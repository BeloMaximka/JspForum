package itstep.learning.services.bodyparser;

import itstep.learning.expections.HttpException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface BodyParseService {
    <T> T parseAndValidate(HttpServletRequest req, Class<T> type) throws HttpException, IOException;
}
