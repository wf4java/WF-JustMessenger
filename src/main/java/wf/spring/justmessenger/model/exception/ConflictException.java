package wf.spring.justmessenger.model.exception;

import org.springframework.http.HttpStatus;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;

public class ConflictException extends BadRequestException {

    private final static HttpStatus STATUS = HttpStatus.CONFLICT;

    public ConflictException() {

    }

    public ConflictException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return STATUS;
    }
}
