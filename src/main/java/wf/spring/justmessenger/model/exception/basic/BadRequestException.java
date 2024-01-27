package wf.spring.justmessenger.model.exception.basic;

import com.mongodb.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BadRequestException extends HttpException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException() {

    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    public BadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BadRequestException(BindingResult bindingResult){
        super(getStringFromBindingResult(bindingResult));
    }


    @Override
    public HttpStatus getHttpStatus() {
        return STATUS;
    }

    private static String getStringFromBindingResult(@Nullable BindingResult bindingResult){
        if(bindingResult == null) return null;

        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            if (!stringBuilder.isEmpty()) stringBuilder.append("\n");
            stringBuilder.append(error.getField()).append(" - ").append(error.getDefaultMessage());
        }
        return stringBuilder.toString();
    }

}
