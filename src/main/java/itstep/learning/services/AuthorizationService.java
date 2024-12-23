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
        if (isInRole(req, role)) {
            return;
        }
        throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Insufficient role");
    }

    public boolean isInRole(HttpServletRequest req, String role) throws HttpException {
        List<String> roles = getRolesFromHeaders(req);
        return roles != null && roles.stream().anyMatch(role::equals);
    }

    private List<String> getRolesFromHeaders(HttpServletRequest request) throws HttpException {
        String accessToken = jwtService.getToken(request);
        DecodedJWT decodedJWT = jwtService.verifyToken(accessToken, new String[]{"roles"});
        return decodedJWT.getClaim("roles").asList(String.class);
    }
}
