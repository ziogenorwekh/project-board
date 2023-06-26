package com.example.projectboard.handler;

import com.example.projectboard.exception.CustomizedResponseException;
import com.example.projectboard.vo.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
            , HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        ExceptionResponse response = new ExceptionResponse(errors.get(0).getDefaultMessage(), new Date());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(Exception e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(CustomizedResponseException.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(CustomizedResponseException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), new Date());
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

}
