package itstep.learning.servlets.posts;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.PostDao;
import itstep.learning.services.PathParserService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Singleton
public class ThemePostsServlet extends RestServlet {
    private final PostDao postDao;
    private final PathParserService pathParserService;

    @Inject
    public ThemePostsServlet(PostDao postDao, PathParserService pathParserService) {
        this.postDao = postDao;
        this.pathParserService = pathParserService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID sectionId = pathParserService.getUUIDAfterSection(req, "themes");
        send(resp, postDao.getAll(sectionId));
    }
}
