package itstep.learning.servlets.themes;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.ThemeDao;
import itstep.learning.models.themes.UpdateThemeModel;
import itstep.learning.services.AuthorizationService;
import itstep.learning.services.PathParserService;
import itstep.learning.services.bodyparser.BodyParseService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Singleton
public class ThemeServlet extends RestServlet {
    private final ThemeDao themeDao;
    private final BodyParseService bodyParseService;
    private final AuthorizationService authorizationService;
    private final PathParserService pathParserService;

    @Inject
    public ThemeServlet(ThemeDao themeDao, BodyParseService bodyParseService, AuthorizationService authorizationService, PathParserService pathParserService) {
        this.themeDao = themeDao;
        this.bodyParseService = bodyParseService;
        this.authorizationService = authorizationService;
        this.pathParserService = pathParserService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID id = pathParserService.getUUIDAfterSection(req, "themes");
        send(resp, themeDao.get(id));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authorizationService.validateRole(req, "admin");
        UUID id = pathParserService.getUUIDAfterSection(req, "themes");

        UpdateThemeModel themeData = bodyParseService.parseAndValidate(req, UpdateThemeModel.class);
        themeDao.update(id, themeData);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        authorizationService.validateRole(req, "admin");
        UUID id = pathParserService.getUUIDAfterSection(req, "themes");

        themeDao.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
