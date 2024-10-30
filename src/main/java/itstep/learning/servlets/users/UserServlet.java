package itstep.learning.servlets.users;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.models.user.UserResponseModel;
import itstep.learning.services.PathParserService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class UserServlet extends RestServlet {

    private final PathParserService pathParserService;
    private final UserDao userDao;

    @Inject
    public UserServlet(PathParserService pathParserService, UserDao userDao) {
        this.pathParserService = pathParserService;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = pathParserService.getStringAfterSection(req, "users");
        send(resp, new UserResponseModel(userDao.get(userName)));
    }
}
