package itstep.learning.servlets;

import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Singleton
public class NotFoundServlet extends RestServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws HttpException {
        sendInvalidPathError();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws HttpException {
        sendInvalidPathError();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws HttpException {
        sendInvalidPathError();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws HttpException {
        sendInvalidPathError();
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] supportedMethods = {"GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS", "TRACE"};
        if (Arrays.stream(supportedMethods).noneMatch(supportedMethod -> supportedMethod.equals(req.getMethod()))) {
            sendInvalidPathError();
        }
        super.service(req, resp);
    }

    private void sendInvalidPathError() throws HttpException {
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Invalid path.");
    }
}
