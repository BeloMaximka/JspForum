package itstep.learning.ioc;

import com.google.inject.servlet.ServletModule;
import itstep.learning.filters.BasicAuthenticationFilter;
import itstep.learning.filters.GlobalErrorHandlingFilter;
import itstep.learning.servlets.HomeServlet;
import itstep.learning.servlets.NotFoundServlet;

public class WebModule  extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("*").through(GlobalErrorHandlingFilter.class);

        filter("/home").through(BasicAuthenticationFilter.class);
        serve("/home").with(HomeServlet.class);

        serve("*").with(NotFoundServlet.class);
    }
}
