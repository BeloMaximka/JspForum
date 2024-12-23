package itstep.learning.servlets.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.expections.HttpException;
import itstep.learning.models.auth.JwtAccessTokenPayload;
import itstep.learning.models.auth.RegisterRequest;
import itstep.learning.models.user.CreateUserModel;
import itstep.learning.services.AuthenticationService;
import itstep.learning.services.LocalStorageService;
import itstep.learning.servlets.RestServlet;
import itstep.learning.services.bodyparser.BodyParseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@Singleton
public class RegisterServlet extends RestServlet {
    private final AuthenticationService authenticationService;
    private final UserDao userDao;
    private final BodyParseService bodyParseService;
    private final LocalStorageService localStorageService;

    @Inject
    public RegisterServlet(AuthenticationService authenticationService,
                           UserDao userDao,
                           @Named("Multipart") BodyParseService bodyParseService,
                           LocalStorageService localStorageService) {
        this.authenticationService = authenticationService;
        this.userDao = userDao;
        this.bodyParseService = bodyParseService;
        this.localStorageService = localStorageService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RegisterRequest reqisterData = bodyParseService.parseAndValidate(req, RegisterRequest.class);

        if (!reqisterData.getPassword().equals(reqisterData.getConfirmPassword())) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Passwords do not match");
        }

        String avatarUrl = null;
        if(reqisterData.getAvatar() != null) {
            avatarUrl = localStorageService.saveFile(reqisterData.getAvatar());
        }
        createUserInDb(reqisterData, avatarUrl);
        setRefreshTokenAndSendAccessToken(reqisterData, resp);
    }

    private void setRefreshTokenAndSendAccessToken(RegisterRequest reqisterData, HttpServletResponse resp) throws IOException {
        JwtAccessTokenPayload user = new JwtAccessTokenPayload();
        user.setEmail(reqisterData.getEmail());
        user.setUsername(reqisterData.getUsername());
        authenticationService.setRefreshTokenInCookie(resp, user.getUsername());
        send(resp, authenticationService.generateAccessToken(user));
    }

    private void createUserInDb(RegisterRequest reqisterData, String avatarUrl) throws ServletException {
        CreateUserModel userModel = new CreateUserModel();
        userModel.setUserName(reqisterData.getUsername());
        userModel.setEmail(reqisterData.getEmail());
        Date sqlDate = new java.sql.Date(reqisterData.getBirthdate().getTime());
        userModel.setBirthdate(sqlDate);
        String passwordHash = BCrypt.withDefaults().hashToString(12, reqisterData.getPassword().toCharArray());
        userModel.setPasswordHash(passwordHash);
        userModel.setAvatarUrl(avatarUrl);
        userDao.create(userModel);
    }
}
