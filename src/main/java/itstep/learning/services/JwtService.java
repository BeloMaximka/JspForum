package itstep.learning.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;
import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Map;

@Singleton
public class JwtService {
    private static final String secret = "62sKhO5yJurIjXbrKDAEdhlzXIo1TlydujKfW451HMQpwKbwzGz6YI8rzf0brqiE";
    private static final Algorithm algorithm = Algorithm.HMAC256(secret);

    public String generateToken(Instant expirationDate, Map<String, Object> claims) {
        return JWT.create()
                .withIssuer("todoissuer")
                .withIssuedAt(Instant.now())
                .withAudience("todoaudience")
                .withExpiresAt(expirationDate)
                .withPayload(claims)
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token, String[] requiredClaims) throws HttpException {
        try {
            Verification verification = JWT.require(algorithm);
            for (String requiredClaim : requiredClaims) {
                verification = verification.withClaimPresence(requiredClaim);
            }
            JWTVerifier verifier = verification.build();
            return verifier.verify(token);
        } catch (Exception e) {
            throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
        }
    }
}
