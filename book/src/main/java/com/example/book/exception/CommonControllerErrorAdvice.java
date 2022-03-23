package com.example.book.exception;

import com.example.book.dto.ErrorResponse;
import com.example.book.errors.InvalidParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class CommonControllerErrorAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidParameterException.class)
    public ErrorResponse handleInvalidParamterException(InvalidParameterException e){
        return ErrorResponse.create().message(e.getMessage()).errors(e.getErrors());
    }
}
