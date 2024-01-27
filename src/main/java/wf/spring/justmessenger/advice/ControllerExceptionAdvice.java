package wf.spring.justmessenger.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import wf.spring.justmessenger.dto.ErrorMessageRsDTO;
import wf.spring.justmessenger.model.exception.basic.BasicException;
import wf.spring.justmessenger.model.exception.basic.HttpException;


@RestControllerAdvice
public class ControllerExceptionAdvice {


    @ExceptionHandler
    private ResponseEntity<ErrorMessageRsDTO> exceptionHandler(HttpException e){
        return new ResponseEntity<>(new ErrorMessageRsDTO(e.getMessage()), e.getHttpStatus());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ErrorMessageRsDTO exceptionHandler(AuthenticationException e){
        return new ErrorMessageRsDTO(e);
    }


    @MessageExceptionHandler
    @SendToUser("/queue/error")
    private ErrorMessageRsDTO messageExceptionHandler(BasicException e) {
        return new ErrorMessageRsDTO(e.getMessage());
    }



}
