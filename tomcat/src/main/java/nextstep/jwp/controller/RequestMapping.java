package nextstep.jwp.controller;

import java.util.HashMap;
import java.util.Map;
import org.apache.coyote.http11.http.HttpRequest;

public class RequestMapping {

    private static final Map<String, Controller> controllers = new HashMap<>();
    private static final ResourceController RESOURCE_CONTROLLER = new ResourceController();

    static {
        controllers.put("/", new HomeController());
        controllers.put("/login", new LoginController());
        controllers.put("/register", new RegisterController());
    }

    public Controller getController(final HttpRequest httpRequest) {
        String uri = httpRequest.getUri();
        return controllers.getOrDefault(uri, RESOURCE_CONTROLLER);
    }
}
