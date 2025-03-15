package com.deiz0n.studfit.controllers.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.deiz0n.studfit.domain.exceptions.aluno.AlunoNotEfetivadoException;
import com.deiz0n.studfit.domain.exceptions.horario.HorarioNotValidException;
import com.deiz0n.studfit.domain.exceptions.resource.ResourceAlreadyException;
import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotExistingException;
import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotFoundException;
import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotValidException;
import com.deiz0n.studfit.domain.exceptions.usuario.CargoNotExistentException;
import com.deiz0n.studfit.domain.exceptions.usuario.SendEmailException;
import com.deiz0n.studfit.domain.exceptions.usuario.SenhaNotCoincideException;
import com.deiz0n.studfit.domain.response.ResponseError;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@ControllerAdvice
public class HandlerExceptionController extends ResponseEntityExceptionHandler{

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

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof MismatchedInputException)
            return handleInvalidFormatException((MismatchedInputException) rootCause, headers, status, request);

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

    private ResponseEntity<Object> handleInvalidFormatException(MismatchedInputException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String fieldName = ex.getPath()
                .stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));

        var detail = String.format("O campo '%s' aceita somentes valores do tipo '%S'", fieldName, ex.getTargetType().getSimpleName());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .title("Campo inválido")
                                .status(HttpStatus.NOT_FOUND)
                                .description(detail)
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

    @ExceptionHandler(ResourceNotExistingException.class)
    public ResponseEntity<ResponseError> handleCargoNotExistException(ResourceNotExistingException exception) {
        var response =  ResponseError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .description(exception.getMessage())
                .build();

        if (exception instanceof CargoNotExistentException) {
            response.setTitle("Cargo inexistente");
        } else {
            response.setTitle("Turno inexistente");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ResourceNotValidException.class)
    public ResponseEntity<ResponseError> handleHorarioNotValidException(ResourceNotValidException exception) {
        var response = ResponseError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .description(exception.getMessage())
                .build();

        if (exception instanceof HorarioNotValidException) {
            response.setTitle("Horário inválido");
        } else {
            response.setTitle("Presença invália");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(AlunoNotEfetivadoException.class)
    public ResponseEntity<ResponseError> handleAlunoNotEfetivadoException(AlunoNotEfetivadoException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .title("Erro ao cadastrar presença")
                                .status(HttpStatus.BAD_REQUEST)
                                .description(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.UNAUTHORIZED.value())
                                .title("Erro na autenticação")
                                .status(HttpStatus.UNAUTHORIZED)
                                .description(exception.getMessage() + " - " + exception.getClass())
                                .build()
                );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.UNAUTHORIZED.value())
                                .title("Acesso negado")
                                .status(HttpStatus.UNAUTHORIZED)
                                .description("O usuário não tem permissão para acessar este recurso")
                                .build()
                );
    }

    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<ResponseError> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.FORBIDDEN.value())
                                .title("Credenciais inválidas")
                                .status(HttpStatus.FORBIDDEN)
                                .description("Usuário ou senha inválidos")
                                .build()
                );
    }

    @ExceptionHandler({InsufficientAuthenticationException.class, JWTVerificationException.class, TokenExpiredException.class})
    public ResponseEntity<ResponseError> handleInsufficientAuthenticationException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.FORBIDDEN.value())
                                .title("Token invdálido")
                                .status(HttpStatus.FORBIDDEN)
                                .description("Token inválido, nulo ou expirado")
                                .build()
                );
    }

    @ExceptionHandler(HorarioNotValidException.class)
    public ResponseEntity<ResponseError> handleHorarioNotValidException(HorarioNotValidException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .title("Horário indisponível")
                                .status(HttpStatus.BAD_REQUEST)
                                .description(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(SenhaNotCoincideException.class)
    public ResponseEntity<ResponseError> handleSenhaNotCoincideException(SenhaNotCoincideException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .title("Senha inválida")
                                .status(HttpStatus.BAD_REQUEST)
                                .description(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(SendEmailException.class)
    public ResponseEntity<ResponseError> handleSendEmaiException(SendEmailException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .title("Erro ao enviar email")
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .description(exception.getMessage())
                                .build()
                );
    }
}
