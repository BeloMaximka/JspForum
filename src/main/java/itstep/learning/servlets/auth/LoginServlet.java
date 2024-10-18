package itstep.learning.servlets.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.dal.dto.User;
import itstep.learning.expections.HttpException;
import itstep.learning.models.auth.JwtAccessTokenPayload;
import itstep.learning.models.auth.LoginRequest;
import itstep.learning.services.AuthService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class LoginServlet extends RestServlet {
    private final UserDao userDao;
    private final AuthService authService;

    @Inject
    public LoginServlet(UserDao userDao, AuthService authService) {
        this.userDao = userDao;
        this.authService = authService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws HttpException, IOException {

        LoginRequest loginData = parseAndValidateBody(req, LoginRequest.class);
        final String errorMsg = "Invalid username or password";
        try {
            User user = userDao.get(loginData.getUsername());
            BCrypt.Result result = BCrypt.verifyer().verify(loginData.getPassword().toCharArray(), user.getPasswordHash().toCharArray());
            if (!result.verified) {
                throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, errorMsg);
            }

            JwtAccessTokenPayload payload = new JwtAccessTokenPayload();
            payload.setEmail(user.getEmail());
            payload.setUsername(user.getUserName());

            authService.setRefreshTokenInCookie(resp, user.getUserName());
            send(resp, authService.generateAccessToken(payload));

        } catch (Exception e) {
            throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, errorMsg);
        }
    }
}
