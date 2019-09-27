package controller;

import controller.methods.ControllerMethod;
import controller.methods.ProcessLoginMethod;
import http.request.Request;
import http.request.RequestMethod;
import http.response.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class LoginController implements Controller {
    private List<RequestMethod> allowedMethods = Arrays.asList(RequestMethod.POST);
    private List<String> allowedUrlPaths = Arrays.asList("/user/login");
    private List<ControllerMethod> loginControllerMethods = Arrays.asList(new ProcessLoginMethod());

    @Override
    public boolean isMapping(ControllerMapper controllerMapper) {
        return (isAllowedMethod(controllerMapper.getRequestMethod())
                && isAllowedUrlPath(controllerMapper.getOriginalUrlPath()));
    }

    private boolean isAllowedUrlPath(String originalUrlPath) {
        return allowedUrlPaths.stream()
                .anyMatch(originalUrlPath::contains);
    }

    private boolean isAllowedMethod(RequestMethod requestMethod) {
        return allowedMethods.stream()
                .anyMatch(method -> method == requestMethod);
    }

    @Override
    public void processResponse(Request request, Response response) throws IOException, URISyntaxException {
        ControllerMethod controllerMethod = loginControllerMethods.stream()
                .filter(method -> method.isMapping(request))
                .findAny()
                .orElseThrow(IllegalAccessError::new);

        controllerMethod.processResponse(request, response);
    }
}
