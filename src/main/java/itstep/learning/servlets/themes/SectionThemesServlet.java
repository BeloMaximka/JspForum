package itstep.learning.servlets.themes;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.ThemeDao;
import itstep.learning.models.themes.ThemeResponseModel;
import itstep.learning.services.PathParserService;
import itstep.learning.services.SlugService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class SectionThemesServlet extends RestServlet {
    private final ThemeDao themeDao;
    private final PathParserService pathParserService;
    private final SlugService slugService;

    @Inject
    public SectionThemesServlet(ThemeDao themeDao, PathParserService pathParserService, SlugService slugService) {
        this.themeDao = themeDao;
        this.pathParserService = pathParserService;
        this.slugService = slugService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID sectionId = pathParserService.getUUIDAfterSection(req, "sections");
        List<ThemeResponseModel> result = themeDao
                .getAll(sectionId)
                .stream().map(theme -> {
                    ThemeResponseModel mapped = new ThemeResponseModel(theme);
                    mapped.setSlug(slugService.slugify(theme.getTitle()));
                    return mapped;
                })
                .collect(Collectors.toList());

        send(resp, themeDao.getAll(sectionId));
    }
}
