package ru.practicum.ewm.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NoEntityException;


import javax.validation.ConstraintViolationException;
import java.util.List;
@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolation(final  DataIntegrityViolationException e, WebRequest request){
        log.info("Error to create entity with not unique parameters.");
        return ErrorResponse.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated")
                .message(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("Input data  is not valid. Error:{}", e.getFieldError().getDefaultMessage());
        return ErrorResponse.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.BAD_REQUEST)
                .reason("For the requested operation the conditions are not met.")
                .message(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(
            ConstraintViolationException e) {
        log.error("Input data  is not valid. Error:{}", e.getLocalizedMessage());
        return ErrorResponse.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.BAD_REQUEST)
                .reason("For the requested operation the conditions are not met.")
                .message(e.getLocalizedMessage())
                .build();

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoEntityException(final NoEntityException e) {
        log.error("Entity not found. Error:{}", e.getMessage());
        return ErrorResponse.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException (final ForbiddenException e) {
        log.error("Forbidden to execute. Error:{}. ", e.getMessage());
        return ErrorResponse.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.FORBIDDEN)
                .reason("For the requested operation the conditions are not met.")
                .message(e.getLocalizedMessage())
                .build();
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalError (final Exception e, WebRequest request){
        log.error("Server error");
        return ErrorResponse.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("Error occurred : " + request.getDescription(false) )
                .message(e.getLocalizedMessage())
                .build();
    }*/
}
