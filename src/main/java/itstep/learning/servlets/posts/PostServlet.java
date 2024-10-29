package itstep.learning.servlets.posts;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.PostDao;
import itstep.learning.models.posts.UpdatePostModel;
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
public class PostServlet extends RestServlet {
    private final PostDao postDao;
    private final BodyParseService bodyParseService;
    private final AuthorizationService authorizationService;
    private final PathParserService pathParserService;

    @Inject
    public PostServlet(PostDao postDao, BodyParseService bodyParseService, AuthorizationService authorizationService, PathParserService pathParserService) {
        this.postDao = postDao;
        this.bodyParseService = bodyParseService;
        this.authorizationService = authorizationService;
        this.pathParserService = pathParserService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID id = pathParserService.getUUIDAfterSection(req, "posts");
        send(resp, postDao.get(id));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authorizationService.validateRole(req, "admin");
        UUID id = pathParserService.getUUIDAfterSection(req, "posts");

        UpdatePostModel postData = bodyParseService.parseAndValidate(req, UpdatePostModel.class);
        postDao.update(id, postData);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        authorizationService.validateRole(req, "admin");
        UUID id = pathParserService.getUUIDAfterSection(req, "posts");

        postDao.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
