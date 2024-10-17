package itstep.learning.filters;

import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Singleton
public class BasicAuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        String credentials = getCredentials((HttpServletRequest) servletRequest);
        String[] decodedCredentials = getDecodedCredentials(credentials);
        ValidateCredentials(decodedCredentials[0], decodedCredentials[1]);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private static String getCredentials(HttpServletRequest servletRequest) throws ServletException {
        String authHeader = servletRequest.getHeader("Authorization");
        if (authHeader == null) {
            throw new ServletException("", new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header not found"));
        }
        String authScheme = "Basic ";
        if (!authHeader.startsWith(authScheme)) {
            throw new ServletException("", new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization scheme. Required " + authScheme));
        }
        return authHeader.substring(authScheme.length());
    }

    private static String[] getDecodedCredentials(String credentials) throws ServletException {
        String decodedCredentials;
        try {
            decodedCredentials = new String(
                    Base64.getUrlDecoder().decode(credentials.getBytes(StandardCharsets.UTF_8)),
                    StandardCharsets.UTF_8
            );
        } catch (IllegalArgumentException ignored) {
            throw new ServletException("", new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization credentials format"));
        }
        String[] parts = decodedCredentials.split(":", 2);
        if (parts.length != 2) {
            throw new ServletException("", new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization credentials composition"));
        }

        return parts;
    }

    private static void ValidateCredentials(String username, String password) throws ServletException {
        if (!username.equals("admin") || !password.equals("password")) {
            throw new ServletException("", new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Credentials rejected"));
        }
    }
}
