package com.deiz0n.studfit.controller.exceptions;

import com.deiz0n.studfit.domain.enums.Cargo;
import com.deiz0n.studfit.domain.exceptions.CargoNotExistentException;
import com.deiz0n.studfit.domain.exceptions.HorarioNotValidException;
import com.deiz0n.studfit.domain.exceptions.ResourceAlreadyException;
import com.deiz0n.studfit.domain.exceptions.ResourceNotFoundException;
import com.deiz0n.studfit.domain.response.ResponseError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;

@RestControllerAdvice
public class HandlerExceptionController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .title("Erro ao realizar a requesição")
                                .status(HttpStatus.BAD_REQUEST)
                                .description(ex.getFieldError().getDefaultMessage())
                                .build()
                );
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .title("Recurso não encontrado")
                                .status(HttpStatus.NOT_FOUND)
                                .description("Rota inexistente")
                                .build()
                );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .title("JSON inválido")
                                .status(HttpStatus.BAD_REQUEST)
                                .description(ex.getMessage())
                                .build()
                );

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseError> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .title("Recurso não encontrado")
                                .status(HttpStatus.NOT_FOUND)
                                .description(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ResourceAlreadyException.class)
    public ResponseEntity<ResponseError> handleResourceNotFoundException(ResourceAlreadyException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .title("Recurso existente")
                                .status(HttpStatus.BAD_REQUEST)
                                .description(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(CargoNotExistentException.class)
    public ResponseEntity<ResponseError> handleCargoNotExistException(CargoNotExistentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .title("Cargo inexistente")
                                .status(HttpStatus.BAD_REQUEST)
                                .description(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(HorarioNotValidException.class)
    private ResponseEntity<ResponseError> handleHorarioNotValidException(HorarioNotValidException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .title("Horário inválido")
                                .status(HttpStatus.BAD_REQUEST)
                                .description(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
