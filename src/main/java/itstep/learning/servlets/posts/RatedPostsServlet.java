package itstep.learning.servlets.posts;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.PostDao;
import itstep.learning.models.auth.JwtAccessTokenPayload;
import itstep.learning.models.posts.PostResponseModel;
import itstep.learning.services.AuthenticationService;
import itstep.learning.services.SlugService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class RatedPostsServlet extends RestServlet {
    private final PostDao postDao;
    private final AuthenticationService authenticationService;
    private final SlugService slugService;

    @Inject
    public RatedPostsServlet(PostDao postDao, AuthenticationService authenticationService, SlugService slugService) {
        this.postDao = postDao;
        this.authenticationService = authenticationService;
        this.slugService = slugService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JwtAccessTokenPayload userInfo = authenticationService.getUserInfoFromHeaders(req);
        List<PostResponseModel> result = postDao
                .getRated(userInfo.getId())
                .stream().map(post -> {
                    PostResponseModel mapped = new PostResponseModel(post);
                    mapped.setSlug(slugService.slugify(post.getTitle()));
                    return mapped;
                })
                .collect(Collectors.toList());

        send(resp, result);
    }
}
