package itstep.learning.servlets.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.dal.dto.User;
import itstep.learning.expections.HttpException;
import itstep.learning.models.auth.JwtAccessTokenPayload;
import itstep.learning.services.AuthService;
import itstep.learning.services.JwtService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Singleton
public class RefreshTokenServlet extends RestServlet {
    private final UserDao userDao;
    private final AuthService authService;
    private final JwtService jwtService;

    @Inject
    public RefreshTokenServlet(UserDao userDao, AuthService authService, JwtService jwtService) {
        this.userDao = userDao;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getUserNameFromRefreshToken(req);
        try {
            User user = userDao.get(userName);

            JwtAccessTokenPayload userPayload = new JwtAccessTokenPayload();
            userPayload.setUsername(user.getUserName());
            userPayload.setEmail(userName);

            authService.setRefreshTokenInCookie(resp, user.getUserName());
            send(resp, authService.generateAccessToken(userPayload));
        } catch (HttpException e) {
            throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
        }
    }

    private String getUserNameFromRefreshToken(HttpServletRequest req) throws HttpException {
        Cookie cookie = Arrays
                .stream(req.getCookies())
                .filter(x -> x.getName().equals("refresh_token"))
                .findFirst()
                .orElse(null);
        if (cookie == null) {
            throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token not found");
        }
        String refreshToken = cookie.getValue();
        DecodedJWT decodedJWT = jwtService.verifyToken(refreshToken, new String[0]);
        String userName = decodedJWT.getClaim("userName").asString();
        if (userName == null) {
            throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
        }
        return userName;
    }
}
