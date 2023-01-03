package nextstep.org.apache.coyote.http11;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.apache.coyote.http11.Http11Processor;
import org.junit.jupiter.api.Test;
import support.HttpFactory;
import support.StubSocket;

class Http11ProcessorTest {

    @Test
    void process() {
        // given
        final var socket = new StubSocket();
        final var processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        var expected = String.join("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Type: text/plain;charset=utf-8 ",
                "Content-Length: 12 ",
                "",
                "Hello world!");

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void index() throws IOException {
        // given
        final String httpRequest = HttpFactory.get("/index.html");

        final var socket = new StubSocket(httpRequest);
        final Http11Processor processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        final URL resource = getClass().getClassLoader()
                .getResource("static/index.html");
        String messageBody = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: " + messageBody.getBytes(StandardCharsets.UTF_8).length + " \r\n" +
                "\r\n" +
                messageBody;

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void login() throws IOException {
        // given
        final String httpRequest = HttpFactory.get("/login");

        final var socket = new StubSocket(httpRequest);
        final Http11Processor processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        final URL resource = getClass().getClassLoader()
                .getResource("static/login.html");
        String messageBody = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: " + messageBody.getBytes(StandardCharsets.UTF_8).length + " \r\n" +
                "\r\n" +
                messageBody;

        assertThat(socket.output()).isEqualTo(expected);
    }
}
