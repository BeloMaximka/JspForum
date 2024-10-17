package itstep.learning.servlets;

import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class NotFoundServlet extends RestServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Invalid path.");
    }
}
