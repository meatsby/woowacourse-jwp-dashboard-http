package org.apache.coyote.http11.http;

import org.apache.coyote.http11.http.domain.ContentType;
import org.apache.coyote.http11.http.domain.Headers;
import org.apache.coyote.http11.http.domain.HttpConstants;
import org.apache.coyote.http11.http.domain.HttpVersion;
import org.apache.coyote.http11.http.domain.MessageBody;
import org.apache.coyote.http11.http.domain.StatusCode;
import org.apache.coyote.http11.http.domain.StatusLine;

public class HttpResponse {

    private final StatusLine statusLine;
    private final Headers headers;
    private final MessageBody messageBody;

    private HttpResponse(final StatusLine statusLine, final Headers headers, final MessageBody messageBody) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.messageBody = messageBody;
    }

    public static HttpResponse ok(final ContentType contentType, final MessageBody messageBody) {
        return new HttpResponse(
                new StatusLine(HttpVersion.HTTP_1_1, StatusCode.OK),
                Headers.builder()
                        .contentType(contentType)
                        .contentLength(messageBody.length()),
                messageBody);
    }

    public byte[] toBytes() {
        return String.join(HttpConstants.CRLF,
                statusLine.getStatusLine(),
                headers.getHeaders(),
                HttpConstants.BLANK,
                messageBody.getValue()).getBytes();
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public Headers getHeaders() {
        return headers;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
