package itstep.learning.servlets.sections;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.SectionDao;
import itstep.learning.models.sections.CreateSectionModel;
import itstep.learning.services.AuthorizationService;
import itstep.learning.services.bodyparser.BodyParseService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class SectionsServlet extends RestServlet {
    private final SectionDao sectionDao;
    private final AuthorizationService authorizationService;
    private final BodyParseService bodyParseService;

    @Inject
    public SectionsServlet(SectionDao sectionDao, AuthorizationService authorizationService, BodyParseService bodyParseService) {
        this.sectionDao = sectionDao;
        this.authorizationService = authorizationService;
        this.bodyParseService = bodyParseService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        send(resp, sectionDao.getAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authorizationService.validateRole(req, "admin");
        CreateSectionModel sectionData = bodyParseService.parseAndValidate(req, CreateSectionModel.class);
        sectionDao.create(sectionData);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
