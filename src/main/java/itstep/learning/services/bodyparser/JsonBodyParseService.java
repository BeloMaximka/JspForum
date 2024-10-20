package itstep.learning.services.bodyparser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import itstep.learning.annotations.Optional;
import itstep.learning.expections.HttpException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JsonBodyParseService implements BodyParseService {
    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    public <T> T parseAndValidate(HttpServletRequest req, Class<T> type) throws HttpException, IOException {
        T body = gson.fromJson(req.getReader(), type);

        if(body == null) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Invalid body type, expected JSON");
        }

        List<String> errors = new ArrayList<>();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            Annotation requiredAnnotation = field.getAnnotation(Optional.class);
            field.setAccessible(true);
            try {
                if (requiredAnnotation == null && field.get(body) == null) {
                    errors.add(field.getName() + " is required");
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        if (!errors.isEmpty()) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, errors);
        }

        return body;
    }
}
