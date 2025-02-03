package com.neurotech.currencyAPI.Infra;

import com.neurotech.currencyAPI.Exception.CambioNotFoundException;
import com.neurotech.currencyAPI.Exception.DateNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CambioExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CambioNotFoundException.class)
    private ResponseEntity<ErrorResponse> CambioNotFoundHandler(CambioNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage(),
                HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(DateNotValidException.class)
    private ResponseEntity<ErrorResponse> DateNotValidHandler(DateNotValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage(),
                HttpStatus.BAD_REQUEST));
    }

}
