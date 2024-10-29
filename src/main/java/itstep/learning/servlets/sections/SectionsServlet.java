package itstep.learning.servlets.sections;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.SectionDao;
import itstep.learning.models.posts.PostResponseModel;
import itstep.learning.models.sections.CreateSectionModel;
import itstep.learning.models.sections.SectionResponseModel;
import itstep.learning.services.AuthorizationService;
import itstep.learning.services.SlugService;
import itstep.learning.services.bodyparser.BodyParseService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class SectionsServlet extends RestServlet {
    private final SectionDao sectionDao;
    private final AuthorizationService authorizationService;
    private final BodyParseService bodyParseService;
    private final SlugService slugService;

    @Inject
    public SectionsServlet(SectionDao sectionDao,
                           AuthorizationService authorizationService,
                           BodyParseService bodyParseService,
                           SlugService slugService) {
        this.sectionDao = sectionDao;
        this.authorizationService = authorizationService;
        this.bodyParseService = bodyParseService;
        this.slugService = slugService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<SectionResponseModel> result = sectionDao
                .getAll()
                .stream().map(section -> {
                    SectionResponseModel mapped = new SectionResponseModel(section);
                    mapped.setSlug(slugService.slugify(section.getTitle()));
                    return mapped;
                })
                .collect(Collectors.toList());
        
        send(resp, result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authorizationService.validateRole(req, "admin");
        CreateSectionModel sectionData = bodyParseService.parseAndValidate(req, CreateSectionModel.class);
        sectionDao.create(sectionData);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
