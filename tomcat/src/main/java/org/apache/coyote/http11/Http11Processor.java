package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import nextstep.jwp.controller.Controller;
import nextstep.jwp.controller.RequestMapping;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.http.HttpRequest;
import org.apache.coyote.http11.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (InputStream inputStream = connection.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             OutputStream outputStream = connection.getOutputStream();
             BufferedWriter bufferedWriter = new BufferedWriter(
                     new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {

            HttpRequest httpRequest = HttpRequest.from(bufferedReader);
            HttpResponse httpResponse = HttpResponse.from(bufferedWriter);

            RequestMapping requestMapping = new RequestMapping();
            Controller controller = requestMapping.getController(httpRequest);
            controller.service(httpRequest, httpResponse);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
