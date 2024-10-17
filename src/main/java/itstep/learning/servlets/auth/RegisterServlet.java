package itstep.learning.servlets.auth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;
import itstep.learning.models.auth.JwtAccessTokenPayload;
import itstep.learning.models.auth.JwtTokenResponse;
import itstep.learning.models.auth.RegisterRequest;
import itstep.learning.services.AuthService;
import itstep.learning.servlets.RestServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class RegisterServlet extends RestServlet {
    private final AuthService authService;

    @Inject
    public RegisterServlet(AuthService authService) {
        this.authService = authService;
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws HttpException, IOException {
        RegisterRequest reqisterData = parseAndValidateBody(req, RegisterRequest.class);

        if(!reqisterData.getPassword().equals(reqisterData.getConfirmPassword())) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Passwords do not match");
        }

        JwtAccessTokenPayload user = new JwtAccessTokenPayload();
        user.setEmail(reqisterData.getEmail());
        user.setUsername(reqisterData.getUsername());

        JwtTokenResponse result = authService.authenticateUser(resp, user);
        send(resp, result);

    }
}
