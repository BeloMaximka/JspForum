package itstep.learning.expections;

import java.util.ArrayList;
import java.util.List;

public class HttpException extends Exception {
    private final int code;
    private final List<String> messages;

    public HttpException(int code, String message) {
        this.code = code;
        this.messages = new ArrayList<>(1);
        this.messages.add(message);
    }

    public int getCode() {
        return code;
    }

    public List<String> getMessages() {
        return messages;
    }
}
