package itstep.learning.servlets;

import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class HomeServlet extends RestServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws HttpException {
        throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Exception message");
    }
}
