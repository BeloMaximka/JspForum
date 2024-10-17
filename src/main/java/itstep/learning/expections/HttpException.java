package itstep.learning.expections;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

public class HttpException extends ServletException {
    private final int code;
    private final List<String> messages;

    public HttpException(int code, String message) {
        this.code = code;
        this.messages = new ArrayList<>(1);
        this.messages.add(message);
    }

    public HttpException(int code, List<String> messages) {
        this.code = code;
        this.messages = messages;
    }

    public int getCode() {
        return code;
    }

    public List<String> getMessages() {
        return messages;
    }
}
