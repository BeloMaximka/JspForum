package itstep.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.SectionDao;
import itstep.learning.expections.HttpException;
import itstep.learning.models.sections.CreateSectionModel;
import itstep.learning.services.AuthorizationService;
import itstep.learning.services.bodyparser.BodyParseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Singleton
public class SectionsServlet extends RestServlet {
    private final SectionDao sectionDao;
    private final BodyParseService bodyParseService;
    private final AuthorizationService authorizationService;

    @Inject
    public SectionsServlet(SectionDao sectionDao, BodyParseService bodyParseService, AuthorizationService authorizationService) {
        this.sectionDao = sectionDao;
        this.bodyParseService = bodyParseService;
        this.authorizationService = authorizationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            send(resp, sectionDao.getAll());
            return;
        }
        try {
            UUID id = UUID.fromString(pathInfo.substring(1));
            send(resp, sectionDao.get(id));
        } catch (IllegalArgumentException e) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Id is not a valid UUID");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authorizationService.validateRole(req, "admin");
        CreateSectionModel sectionData = bodyParseService.parseAndValidate(req, CreateSectionModel.class);
        sectionDao.create(sectionData);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authorizationService.validateRole(req, "admin");
        UUID id;
        try {
            id = UUID.fromString(req.getPathInfo().substring(1));

        } catch (IllegalArgumentException e) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Id is not a valid UUID");
        } catch (Exception e) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Id is required");
        }

        CreateSectionModel sectionData = bodyParseService.parseAndValidate(req, CreateSectionModel.class);
        sectionDao.update(id, sectionData);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        authorizationService.validateRole(req, "admin");
        UUID id;
        try {
            id = UUID.fromString(req.getPathInfo().substring(1));

        } catch (IllegalArgumentException e) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Id is not a valid UUID");
        } catch (Exception e) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Id is required");
        }

        sectionDao.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
