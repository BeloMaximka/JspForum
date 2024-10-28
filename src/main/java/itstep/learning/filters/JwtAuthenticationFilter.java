package itstep.learning.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.services.JwtService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
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
        String token = jwtService.getToken((HttpServletRequest) servletRequest);
        DecodedJWT decoded = jwtService.verifyToken(token, new String[0]);
        servletRequest.setAttribute("user", decoded);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
