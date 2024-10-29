package itstep.learning.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;
import itstep.learning.models.auth.JwtTokenResponse;
import itstep.learning.models.auth.JwtAccessTokenPayload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class AuthenticationService {
    private final JwtService jwtService;

    @Inject
    public AuthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public JwtAccessTokenPayload getUserInfoFromHeaders(HttpServletRequest req) throws HttpException {
        DecodedJWT payload = jwtService.verifyToken(jwtService.getToken(req), new String[]{"sub", "roles", "email", "username"});
        JwtAccessTokenPayload userInfo = new JwtAccessTokenPayload();
        userInfo.setId(UUID.fromString(payload.getClaim("sub").asString()));
        userInfo.setUsername(payload.getClaim("username").asString());
        userInfo.setRoles(payload.getClaim("roles").asList(String.class));
        userInfo.setEmail(payload.getClaim("email").asString());
        return userInfo;
    }

    public JwtTokenResponse generateAccessToken(JwtAccessTokenPayload user) {
        Instant oneHourExpiration = Instant.now().plusSeconds(86400);
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getId().toString());
        claims.put("roles", user.getRoles());
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());

        JwtTokenResponse result = new JwtTokenResponse();
        result.setToken(jwtService.generateToken(oneHourExpiration, claims));
        return result;
    }

    public void setRefreshTokenInCookie(HttpServletResponse resp, String userName) {
        int monthDurationInSeconds = 86400 * 30;
        Instant oneMonthExpirationDate = Instant.now().plusSeconds(monthDurationInSeconds);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", userName);

        resp.setHeader("Set-Cookie", String.format("refresh_token=%s; Max-Age=%d; Path=/api/auth/refresh-token; Secure; HttpOnly; SameSite=Strict",
                jwtService.generateToken(oneMonthExpirationDate, claims),
                monthDurationInSeconds));
    }
}
