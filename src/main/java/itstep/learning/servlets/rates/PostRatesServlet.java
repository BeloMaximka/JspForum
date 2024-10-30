package itstep.learning.servlets.rates;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.RateDao;
import itstep.learning.expections.HttpException;
import itstep.learning.models.auth.JwtAccessTokenPayload;
import itstep.learning.models.rates.CreateRateModel;
import itstep.learning.models.rates.UpdateRateModel;
import itstep.learning.services.AuthenticationService;
import itstep.learning.services.PathParserService;
import itstep.learning.services.bodyparser.BodyParseService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Singleton
public class PostRatesServlet extends RestServlet {
    private final PathParserService pathParserService;
    private final BodyParseService bodyParseService;
    private final RateDao rateDao;
    private final AuthenticationService authenticationService;


    @Inject
    public PostRatesServlet(PathParserService pathParserService,
                            BodyParseService bodyParseService,
                            RateDao rateDao,
                            AuthenticationService authenticationService) {
        this.pathParserService = pathParserService;
        this.bodyParseService = bodyParseService;
        this.rateDao = rateDao;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID id = pathParserService.getUUIDAfterSection(req, "posts");
        send(resp, rateDao.getByItemId(id));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateRateModel createRateModel = getRateModel(req);
        if (createRateModel.getRate() < 1 || createRateModel.getRate() > 5) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Rate must be between 1 and 5");
        }
        rateDao.create(createRateModel);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateRateModel createRateModel = getRateModel(req);
        if (createRateModel.getRate() < 1 || createRateModel.getRate() > 5) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Rate must be between 1 and 5");
        }
        rateDao.update(createRateModel);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        UUID postId = pathParserService.getUUIDAfterSection(req, "posts");
        JwtAccessTokenPayload userInfo = authenticationService.getUserInfoFromHeaders(req);

        rateDao.delete(postId, userInfo.getId());
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private CreateRateModel getRateModel(HttpServletRequest req) throws HttpException, IOException {
        UUID postId = pathParserService.getUUIDAfterSection(req, "posts");

        UpdateRateModel rateData = bodyParseService.parseAndValidate(req, UpdateRateModel.class);
        JwtAccessTokenPayload userInfo = authenticationService.getUserInfoFromHeaders(req);

        CreateRateModel createRateModel = new CreateRateModel();
        createRateModel.setRate(rateData.getRate());
        createRateModel.setUserId(userInfo.getId());
        createRateModel.setItemId(postId);
        return createRateModel;
    }
}
