package itstep.learning.servlets;

import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class HomeServlet extends RestServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws HttpException, IOException {
        send(resp, req.getServletPath());
    }
}
