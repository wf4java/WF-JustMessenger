package wf.spring.justmessenger.model.exception.basic;

import org.springframework.http.HttpStatus;

public class ServerException extends HttpException {

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public ServerException() {
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public ServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return STATUS;
    }
}
