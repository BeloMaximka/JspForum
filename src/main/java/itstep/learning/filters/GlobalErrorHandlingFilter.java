package itstep.learning.filters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;
import itstep.learning.models.rest.RestErrorResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class GlobalErrorHandlingFilter implements Filter {
    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch (HttpException e) {
            sendError(servletResponse, e.getCode(), e.getMessages());
        }
        catch (Exception e) {
            List<String> errors = new ArrayList<>(1);
            errors.add("An unknown error occurred. Please try again later.");
            sendError(servletResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errors);
        }
    }

    private void sendError(ServletResponse servletResponse, int code, List<String> messages) throws IOException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        resp.setContentType("application/json");
        resp.setStatus(code);

        RestErrorResponse restErrorResponse = new RestErrorResponse(messages);
        resp.getWriter().print(gson.toJson(restErrorResponse));
    }
}
