package com.example.projectboard.security.filter;

import com.example.projectboard.exception.InvalidateInputDataException;
import com.example.projectboard.vo.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class FilterExceptionHandler {

    @SneakyThrows
    public static void exceptionHandler(String exception, HttpServletResponse response) {
        log.error("error -> {}", exception);

        response.setContentType(APPLICATION_JSON_VALUE);
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception, new Date());
        new ObjectMapper().writeValue(response.getOutputStream(), exceptionResponse);
    }

    public static void exceptionThrow(String message) {
        throw new InvalidateInputDataException(message);
    }

}
