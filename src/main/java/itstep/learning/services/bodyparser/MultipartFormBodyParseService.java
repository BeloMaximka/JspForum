package itstep.learning.services.bodyparser;

import com.google.inject.Inject;
import itstep.learning.annotations.Optional;
import itstep.learning.expections.HttpException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.rmi.ServerException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MultipartFormBodyParseService implements BodyParseService {
    private final static int memoryLimit = 3 * 1024 * 1024;
    private final static int maxSingleFile = 2 * 1024 * 1024;
    private final static int maxFormFile = 5 * 1024 * 1024;
    private final ServletFileUpload servletFileUpload;
    private final String dateFormat = "yyyy-MM-dd";
    private final SimpleDateFormat sqlDateFormat = new SimpleDateFormat(dateFormat);

    @Inject
    public MultipartFormBodyParseService() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(memoryLimit);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        servletFileUpload = new ServletFileUpload(factory);
        servletFileUpload.setFileSizeMax(maxSingleFile);
        servletFileUpload.setSizeMax(maxFormFile);
    }

    @Override
    public <T> T parseAndValidate(HttpServletRequest req, Class<T> type) throws HttpException, IOException {
        if (!ServletFileUpload.isMultipartContent(req)) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Invalid body type, expected multipart/form-data");
        }

        try {
            T body = type.newInstance();

            List<String> errors = new ArrayList<>();
            Field[] fields = type.getDeclaredFields();
            List<FileItem> fileItems;
            fileItems = servletFileUpload.parseRequest(req);

            for (Field field : fields) {
                Annotation optionalAnnotation = field.getAnnotation(Optional.class);
                field.setAccessible(true);
                for (FileItem item : fileItems) {
                    if (!field.getName().equals(item.getFieldName())) {
                        continue;
                    }
                    setParsedField(body, field, item, errors);
                    break;
                }
                if (optionalAnnotation == null && field.get(body) == null) {
                    errors.add(field.getName() + " is required");
                }
            }

            if (!errors.isEmpty()) {
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, errors);
            }

            return body;

        } catch (Exception e) {
            if (e instanceof HttpException) {
                throw (HttpException) e;
            }
            throw new ServerException(e.getMessage());
        }

    }

    private <T> void setParsedField(T body, Field field, FileItem item, List<String> errors) throws Exception {
        if (field.getType() == String.class) {
            field.set(body, item.getString());
        } else if (field.getType() == Date.class) {
            try {
                field.set(body, sqlDateFormat.parse(item.getString()));
            } catch (Exception e) {
                errors.add(String.format("Date field '%s' has incorrect format: '%s'. Expected format: '%s", field.getName(), item.getString(), dateFormat));
            }
        } else if (field.getType() == FileItem.class) {
            if(item.isFormField()) {
                errors.add(String.format("Field '%s' has incorrect format: '%s'. Expected a file", field.getName(), item.getString()));
            }
            field.set(body, item);
        } else {
            throw new ServletException(String.format("Unsupported type '%s' of field '%s'", field.getType(), field.getName()));
        }
    }
}
