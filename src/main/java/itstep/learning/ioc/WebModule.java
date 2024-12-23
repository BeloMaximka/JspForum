package itstep.learning.ioc;

import com.google.inject.servlet.ServletModule;
import itstep.learning.filters.JwtAuthenticationFilter;
import itstep.learning.filters.GlobalErrorHandlingFilter;
import itstep.learning.servlets.NotFoundServlet;
import itstep.learning.servlets.posts.*;
import itstep.learning.servlets.rates.PostRatesServlet;
import itstep.learning.servlets.sections.SectionServlet;
import itstep.learning.servlets.sections.SectionsServlet;
import itstep.learning.servlets.StorageServlet;
import itstep.learning.servlets.auth.LoginServlet;
import itstep.learning.servlets.auth.RefreshTokenServlet;
import itstep.learning.servlets.auth.RegisterServlet;
import itstep.learning.servlets.themes.SectionThemesServlet;
import itstep.learning.servlets.themes.ThemeServlet;
import itstep.learning.servlets.themes.ThemesServlet;
import itstep.learning.servlets.users.MyUserServlet;
import itstep.learning.servlets.users.UserServlet;

public class WebModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("*").through(GlobalErrorHandlingFilter.class);

        serve("/api/users/my").with(MyUserServlet.class);
        serveRegex("/api/users/.+").with(UserServlet.class);

        serve("/api/auth").with(LoginServlet.class);
        serve("/api/auth/register").with(RegisterServlet.class);
        filter("/api/auth/refresh-token").through(JwtAuthenticationFilter.class);
        serve("/api/auth/refresh-token").with(RefreshTokenServlet.class);

        serve("/api/storage/*").with(StorageServlet.class);

        serveRegex("/api/posts/[0-9A-z-]{36}/rates").with(PostRatesServlet.class);

        serveRegex("/api/themes/[0-9A-z-]{36}/posts").with(ThemePostsServlet.class);
        serveRegex("/api/posts/[0-9A-z-]{36}").with(PostServlet.class);
        serveRegex("/api/posts/[0-9A-z-]{36}/similar/\\d").with(SimilarPostsServlet.class);
        serveRegex("/api/posts/rated").with(RatedPostsServlet.class);
        serveRegex("/api/posts").with(PostsServlet.class);

        serveRegex("/api/sections/[0-9A-z-]{36}/themes").with(SectionThemesServlet.class);
        serveRegex("/api/themes/[0-9A-z-]{36}").with(ThemeServlet.class);
        serveRegex("/api/themes").with(ThemesServlet.class);

        serveRegex("/api/sections").with(SectionsServlet.class);
        serveRegex("/api/sections/[0-9A-z-]{36}").with(SectionServlet.class);

        serve("*").with(NotFoundServlet.class);
    }
}
