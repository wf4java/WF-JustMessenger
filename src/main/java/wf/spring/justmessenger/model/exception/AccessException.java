package wf.spring.justmessenger.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;


@Getter
public class AccessException extends BadRequestException {

    private final static HttpStatus STATUS = HttpStatus.FORBIDDEN;

    public AccessException() {

    }

    public AccessException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return STATUS;
    }

}
