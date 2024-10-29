package itstep.learning.servlets.posts;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.PostDao;
import itstep.learning.dal.dto.Post;
import itstep.learning.models.posts.PostResponseModel;
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
public class SimilarPostsServlet extends RestServlet {
    private final PathParserService pathParserService;
    private final PostDao postDao;
    private final SlugService slugService;

    @Inject
    public SimilarPostsServlet(PathParserService pathParserService, PostDao postDao, SlugService slugService) {
        this.pathParserService = pathParserService;
        this.postDao = postDao;
        this.slugService = slugService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID postId = pathParserService.getUUIDAfterSection(req, "posts");
        int id = pathParserService.getIntAfterSection(req, "similar");

        List<PostResponseModel> result = postDao
                .getSimilar(postDao.get(postId), id)
                .stream().map(post -> {
                    PostResponseModel mapped = new PostResponseModel(post);
                    mapped.setSlug(slugService.slugify(post.getTitle()));
                    return mapped;
                })
                .collect(Collectors.toList());
        send(resp, result);
    }
}
