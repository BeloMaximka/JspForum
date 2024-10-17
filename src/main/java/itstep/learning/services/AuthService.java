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
public class AuthService {
    private final JwtService jwtService;

    @Inject
    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public JwtTokenResponse authenticateUser(HttpServletResponse resp, JwtAccessTokenPayload user) {
        String refreshToken = generateRefreshTokenCookieHeader(user.getEmail());
        resp.setHeader("Set-Cookie", refreshToken);
        return generateAccessTokenResponse(user);
    }

    private JwtTokenResponse generateAccessTokenResponse(JwtAccessTokenPayload user) {
        Instant oneHourExpiration = Instant.now().plusSeconds(86400);
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());

        JwtTokenResponse result = new JwtTokenResponse();
        result.setToken(jwtService.generateToken(oneHourExpiration, claims));
        return result;
    }

    private String generateRefreshTokenCookieHeader(String userEmail) {
        int monthDurationInSeconds = 86400 * 30;
        Instant oneMonthExpirationDate = Instant.now().plusSeconds(monthDurationInSeconds);
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userEmail);

        return String.format("refresh_token=%s; Max-Age=%d; Path=/api/auth/refresh-token; Secure; HttpOnly; SameSite=Strict",
                jwtService.generateToken(oneMonthExpirationDate, claims),
                monthDurationInSeconds);
    }
}
