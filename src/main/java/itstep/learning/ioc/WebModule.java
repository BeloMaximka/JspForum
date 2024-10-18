package itstep.learning.ioc;

import com.google.inject.servlet.ServletModule;
import itstep.learning.filters.JwtAuthenticationFilter;
import itstep.learning.filters.GlobalErrorHandlingFilter;
import itstep.learning.servlets.HomeServlet;
import itstep.learning.servlets.NotFoundServlet;
import itstep.learning.servlets.auth.LoginServlet;
import itstep.learning.servlets.auth.RefreshTokenServlet;
import itstep.learning.servlets.auth.RegisterServlet;

public class WebModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("*").through(GlobalErrorHandlingFilter.class);

        filter("/api/home").through(JwtAuthenticationFilter.class);
        serve("/api/home").with(HomeServlet.class);

        serve("/api/auth").with(LoginServlet.class);
        serve("/api/auth/register").with(RegisterServlet.class);
        serve("/api/auth/refresh-token").with(RefreshTokenServlet.class);

        serve("*").with(NotFoundServlet.class);
    }
}
