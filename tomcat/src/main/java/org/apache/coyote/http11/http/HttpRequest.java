package org.apache.coyote.http11.http;

import java.io.BufferedReader;
import java.io.IOException;
import org.apache.catalina.Session;
import org.apache.catalina.SessionManager;
import org.apache.coyote.http11.http.domain.Headers;
import org.apache.coyote.http11.http.domain.HttpCookie;
import org.apache.coyote.http11.http.domain.HttpMethod;
import org.apache.coyote.http11.http.domain.MessageBody;
import org.apache.coyote.http11.http.domain.RequestLine;

public class HttpRequest {

    private final RequestLine requestLine;
    private final Headers headers;
    private final MessageBody messageBody;

    private HttpRequest(final RequestLine requestLine, final Headers headers, final MessageBody messageBody) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.messageBody = messageBody;
    }

    public static HttpRequest from(final BufferedReader bufferedReader) {
        try {
            RequestLine requestLine = RequestLine.from(bufferedReader);
            Headers headers = Headers.from(bufferedReader);
            MessageBody messageBody = MessageBody.from(bufferedReader, headers);
            return new HttpRequest(
                    requestLine,
                    headers,
                    messageBody);
        } catch (IOException e) {
            throw new IllegalArgumentException("HttpRequest creation failed.");
        }
    }

    public boolean isValidCookie() {
        HttpCookie cookie = headers.getCookie();
        String jsessionid = cookie.getCookie("JSESSIONID");
        return cookie.containsJSESSIONID() && SessionManager.contains(jsessionid);
    }

    public Session getSession() {
        HttpCookie cookie = headers.getCookie();
        String jsessionid = cookie.getCookie("JSESSIONID");
        return SessionManager.findSession(jsessionid);
    }

    public HttpMethod getHttpMethod() {
        return requestLine.getHttpMethod();
    }

    public String getUri() {
        return requestLine.getRequestTarget().getUri();
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Headers getHeaders() {
        return headers;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
