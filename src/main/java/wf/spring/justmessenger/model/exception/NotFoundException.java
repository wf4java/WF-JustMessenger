package wf.spring.justmessenger.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;

@Getter
public class NotFoundException extends BadRequestException {

    private final static HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public NotFoundException() {

    }

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return STATUS;
    }
}
