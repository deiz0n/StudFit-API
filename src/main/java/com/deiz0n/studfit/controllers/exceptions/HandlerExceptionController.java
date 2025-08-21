package com.deiz0n.studfit.controllers.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.deiz0n.studfit.domain.exceptions.aluno.AlunoNotEfetivadoException;
import com.deiz0n.studfit.domain.exceptions.aluno.AtestadoNotValidException;
import com.deiz0n.studfit.domain.exceptions.horario.HorarioNotValidException;
import com.deiz0n.studfit.domain.exceptions.resource.ResourceAlreadyException;
import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotExistingException;
import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotFoundException;
import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotValidException;
import com.deiz0n.studfit.domain.exceptions.usuario.CargoNotExistentException;
import com.deiz0n.studfit.domain.exceptions.usuario.SendEmailException;
import com.deiz0n.studfit.domain.exceptions.usuario.SenhaNotCoincideException;
import com.deiz0n.studfit.domain.exceptions.utils.CreationDirectoryException;
import com.deiz0n.studfit.domain.exceptions.utils.InternalServerErrorException;
import com.deiz0n.studfit.domain.response.ResponseError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@ControllerAdvice
public class HandlerExceptionController extends ResponseEntityExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String tamanhoMaximoAtestado;

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

        if (rootCause instanceof UnrecognizedPropertyException)
            return handleUnrecognizedPropertyException((UnrecognizedPropertyException) rootCause, headers, status, request);
        if (rootCause instanceof IgnoredPropertyException)
            return handleUnrecognizedPropertyException((IgnoredPropertyException) rootCause, headers, status, request);
        if (rootCause instanceof MismatchedInputException)
            return handleInvalidFormatException((MismatchedInputException) rootCause, headers, status, request);
        if (rootCause instanceof JsonParseException)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            ResponseError.builder()
                                    .code(HttpStatus.BAD_REQUEST.value())
                                    .title("JSON inválido")
                                    .status(HttpStatus.BAD_REQUEST)
                                    .description("O JSON informado possui formato inválido")
                                    .build()
                    );


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
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .title("Campo inválido")
                                .status(HttpStatus.BAD_REQUEST)
                                .description(detail)
                                .build()
                );
    }

    private ResponseEntity<Object> handleUnrecognizedPropertyException(PropertyBindingException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        String fieldName = ex.getPath()
                .stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining());

        var detail = String.format("O campo '%s' não existe", fieldName);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .title("Campo inválido")
                                .status(HttpStatus.BAD_REQUEST)
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
        var response = ResponseError.builder()
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

        if (exception instanceof HorarioNotValidException)
            response.setTitle("Horário inválido");

        if (exception instanceof AtestadoNotValidException)
            response.setTitle("Atestado inválido");
        else {
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

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ResponseError> handleInternalServerError(InternalServerErrorException exception) {
        var response = ResponseError.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .description(exception.getMessage())
                .build();


        if (exception instanceof CreationDirectoryException)
            response.setTitle("Erro ao criar diretório");
        else
            response.setTitle("Erro ao salvar atestado");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.PAYLOAD_TOO_LARGE.value())
                                .title("Arquivo muito grande")
                                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                                .description(
                                        String.format("O tamanho máximo para um arquivo é %s", tamanhoMaximoAtestado)
                                )
                                .build()
                );
    }
}
