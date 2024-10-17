package itstep.learning.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;
import itstep.learning.services.JwtService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class JwtAuthenticationFilter implements Filter {
    private final JwtService jwtService;

    @Inject
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken((HttpServletRequest) servletRequest);
        DecodedJWT decoded = jwtService.verifyToken(token, new String[0]);
        servletRequest.setAttribute("user", decoded);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private static String getToken(HttpServletRequest servletRequest) throws ServletException {
        String authHeader = servletRequest.getHeader("Authorization");
        if (authHeader == null) {
            throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header not found");
        }
        String authScheme = "Bearer ";
        if (!authHeader.startsWith(authScheme)) {
            throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization scheme. Required " + authScheme);
        }
        return authHeader.substring(authScheme.length());
    }
}
