package itstep.learning.servlets.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.expections.HttpException;
import itstep.learning.models.auth.JwtAccessTokenPayload;
import itstep.learning.models.auth.RegisterRequest;
import itstep.learning.models.user.CreateUserModel;
import itstep.learning.services.AuthService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@Singleton
public class RegisterServlet extends RestServlet {
    private final AuthService authService;
    private final UserDao userDao;

    @Inject
    public RegisterServlet(AuthService authService, UserDao userDao) {
        this.authService = authService;
        this.userDao = userDao;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RegisterRequest reqisterData = parseAndValidateBody(req, RegisterRequest.class);

        if (!reqisterData.getPassword().equals(reqisterData.getConfirmPassword())) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Passwords do not match");
        }

        createUserInDb(reqisterData);
        setRefreshTokenAndSendAccessToken(reqisterData, resp);
    }

    private void setRefreshTokenAndSendAccessToken(RegisterRequest reqisterData, HttpServletResponse resp) throws IOException {
        JwtAccessTokenPayload user = new JwtAccessTokenPayload();
        user.setEmail(reqisterData.getEmail());
        user.setUsername(reqisterData.getUsername());
        authService.setRefreshTokenInCookie(resp, user.getUsername());
        send(resp, authService.generateAccessToken(user));
    }

    private void createUserInDb(RegisterRequest reqisterData) throws ServletException {
        CreateUserModel userModel = new CreateUserModel();
        userModel.setUserName(reqisterData.getUsername());
        userModel.setEmail(reqisterData.getEmail());
        Date sqlDate = new java.sql.Date(reqisterData.getBirthdate().getTime());
        userModel.setBirthdate(sqlDate);
        String passwordHash = BCrypt.withDefaults().hashToString(12, reqisterData.getPassword().toCharArray());
        userModel.setPasswordHash(passwordHash);
        userDao.create(userModel);
    }
}
