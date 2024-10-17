package itstep.learning.models.rest;

import java.util.List;

public class RestErrorResponse {
    private final List<String> errors;

    public RestErrorResponse(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
