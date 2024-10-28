package itstep.learning.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.models.auth.JwtTokenResponse;
import itstep.learning.models.auth.JwtAccessTokenPayload;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class AuthenticationService {
    private final JwtService jwtService;

    @Inject
    public AuthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public JwtTokenResponse generateAccessToken(JwtAccessTokenPayload user) {
        Instant oneHourExpiration = Instant.now().plusSeconds(86400);
        Map<String, Object> claims = new HashMap<>();
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
