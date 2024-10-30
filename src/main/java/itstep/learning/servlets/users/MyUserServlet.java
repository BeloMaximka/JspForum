package itstep.learning.servlets.users;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.models.auth.JwtAccessTokenPayload;
import itstep.learning.models.user.UpdateUserModel;
import itstep.learning.models.user.UserResponseModel;
import itstep.learning.services.AuthenticationService;
import itstep.learning.services.bodyparser.BodyParseService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class MyUserServlet extends RestServlet {
    private final AuthenticationService authenticationService;
    private final UserDao userDao;
    private final BodyParseService bodyParseService;

    @Inject
    public MyUserServlet(AuthenticationService authenticationService, UserDao userDao, BodyParseService bodyParseService) {
        this.authenticationService = authenticationService;
        this.userDao = userDao;
        this.bodyParseService = bodyParseService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JwtAccessTokenPayload userInfo = authenticationService.getUserInfoFromHeaders(req);
        send(resp, new UserResponseModel(userDao.get(userInfo.getUsername())));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JwtAccessTokenPayload userInfo = authenticationService.getUserInfoFromHeaders(req);
        UpdateUserModel userData = bodyParseService.parseAndValidate(req, UpdateUserModel.class);
        userDao.update(userInfo.getId(), userData);

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
