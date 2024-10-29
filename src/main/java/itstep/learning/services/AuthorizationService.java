package itstep.learning.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Singleton
public class AuthorizationService {
    private final JwtService jwtService;

    @Inject
    public AuthorizationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public void validateRole(HttpServletRequest req, String role) throws HttpException {
        List<String> roles = getRolesFromHeaders(req);
        if (roles == null || roles.stream().noneMatch(role::equals)) {
            throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Insufficient role");
        }
    }

    private List<String> getRolesFromHeaders(HttpServletRequest request) throws HttpException {
        String accessToken = jwtService.getToken(request);
        DecodedJWT decodedJWT = jwtService.verifyToken(accessToken, new String[]{"roles"});
        return decodedJWT.getClaim("roles").asList(String.class);
    }
}
