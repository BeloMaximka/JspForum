package itstep.learning.servlets.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.RoleDao;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.dal.dto.Role;
import itstep.learning.dal.dto.User;
import itstep.learning.expections.HttpException;
import itstep.learning.models.auth.JwtAccessTokenPayload;
import itstep.learning.services.AuthenticationService;
import itstep.learning.services.JwtService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class RefreshTokenServlet extends RestServlet {
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Inject
    public RefreshTokenServlet(UserDao userDao, RoleDao roleDao, AuthenticationService authenticationService, JwtService jwtService) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getUserNameFromRefreshToken(req);
        try {
            User user = userDao.get(userName);

            JwtAccessTokenPayload userPayload = new JwtAccessTokenPayload();
            List<String> roles = roleDao.getAll(user.getUserId()).stream().map(Role::getName).collect(Collectors.toList());
            userPayload.setId(user.getUserId());
            userPayload.setRoles(roles);
            userPayload.setUsername(user.getUserName());
            userPayload.setEmail(userName);

            authenticationService.setRefreshTokenInCookie(resp, user.getUserName());
            send(resp, authenticationService.generateAccessToken(userPayload));
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
