package itstep.learning.servlets.themes;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.ThemeDao;
import itstep.learning.models.themes.CreateThemeModel;
import itstep.learning.services.AuthorizationService;
import itstep.learning.services.bodyparser.BodyParseService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class ThemesServlet extends RestServlet {
    private final ThemeDao themeDao;
    private final AuthorizationService authorizationService;
    private final BodyParseService bodyParseService;

    @Inject
    public ThemesServlet(ThemeDao themeDao, AuthorizationService authorizationService, BodyParseService bodyParseService) {
        this.themeDao = themeDao;
        this.authorizationService = authorizationService;
        this.bodyParseService = bodyParseService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authorizationService.validateRole(req, "admin");
        CreateThemeModel themeData = bodyParseService.parseAndValidate(req, CreateThemeModel.class);
        themeDao.create(themeData);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
