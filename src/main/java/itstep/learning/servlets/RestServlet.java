package itstep.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import itstep.learning.expections.HttpException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class RestServlet extends HttpServlet {
    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendMethodNotAllowedError("POST");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendMethodNotAllowedError("GET");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendMethodNotAllowedError("PUT");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendMethodNotAllowedError("DELETE");
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] supportedMethods = {"GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS", "TRACE"};
        String method = req.getMethod();
        if (Arrays.stream(supportedMethods).noneMatch(supportedMethod -> supportedMethod.equals(method))) {
            sendMethodNotAllowedError(method);
        }
        super.service(req, resp);
    }

    protected void send(HttpServletResponse resp, Object content) throws IOException {
        send(resp, HttpServletResponse.SC_OK, content);
    }

    protected void send(HttpServletResponse resp, int code, Object content) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(code);
        resp.getWriter().print(gson.toJson(content));
    }

    private void sendMethodNotAllowedError(String methodName) throws HttpException {
        throw new HttpException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, String.format("Method '%s' not allowed", methodName));
    }
}
