package itstep.learning.servlets.sections;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.SectionDao;
import itstep.learning.models.sections.CreateSectionModel;
import itstep.learning.models.sections.SectionResponseModel;
import itstep.learning.services.AuthorizationService;
import itstep.learning.services.PathParserService;
import itstep.learning.services.SlugService;
import itstep.learning.services.bodyparser.BodyParseService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Singleton
public class SectionServlet extends RestServlet {
    private final SectionDao sectionDao;
    private final BodyParseService bodyParseService;
    private final AuthorizationService authorizationService;
    private final PathParserService pathParserService;
    private final SlugService slugService;

    @Inject
    public SectionServlet(SectionDao sectionDao,
                          BodyParseService bodyParseService,
                          AuthorizationService authorizationService,
                          PathParserService pathParserService,
                          SlugService slugService) {
        this.sectionDao = sectionDao;
        this.bodyParseService = bodyParseService;
        this.authorizationService = authorizationService;
        this.pathParserService = pathParserService;
        this.slugService = slugService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID id = pathParserService.getUUIDAfterSection(req, "sections");

        SectionResponseModel result = new SectionResponseModel(sectionDao.get(id));
        result.setSlug(slugService.slugify(result.getTitle()));
        send(resp, result);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authorizationService.validateRole(req, "admin");
        UUID id = pathParserService.getUUIDAfterSection(req, "sections");

        CreateSectionModel sectionData = bodyParseService.parseAndValidate(req, CreateSectionModel.class);
        sectionDao.update(id, sectionData);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        authorizationService.validateRole(req, "admin");
        UUID id = pathParserService.getUUIDAfterSection(req, "sections");

        sectionDao.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
