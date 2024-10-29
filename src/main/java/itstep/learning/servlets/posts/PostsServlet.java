package itstep.learning.servlets.posts;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.PostDao;
import itstep.learning.models.auth.JwtAccessTokenPayload;
import itstep.learning.models.posts.CreatePostModel;
import itstep.learning.services.AuthenticationService;
import itstep.learning.services.bodyparser.BodyParseService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class PostsServlet extends RestServlet {
    private final PostDao postDao;
    private final BodyParseService bodyParseService;
    private final AuthenticationService authenticationService;

    @Inject
    public PostsServlet(PostDao postDao, BodyParseService bodyParseService, AuthenticationService authenticationService) {
        this.postDao = postDao;
        this.bodyParseService = bodyParseService;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreatePostModel postData = bodyParseService.parseAndValidate(req, CreatePostModel.class);
        JwtAccessTokenPayload userInfo = authenticationService.getUserInfoFromHeaders(req);
        postData.setAuthorId(userInfo.getId());
        postDao.create(postData);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
