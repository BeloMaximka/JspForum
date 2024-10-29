package itstep.learning.servlets.themes;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.ThemeDao;
import itstep.learning.services.PathParserService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Singleton
public class SectionThemesServlet extends RestServlet {
    private final ThemeDao themeDao;
    private final PathParserService pathParserService;

    @Inject
    public SectionThemesServlet(ThemeDao themeDao, PathParserService pathParserService) {
        this.themeDao = themeDao;
        this.pathParserService = pathParserService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID sectionId = pathParserService.getUUIDAfterSection(req, "sections");
        send(resp, themeDao.getAll(sectionId));
    }
}
