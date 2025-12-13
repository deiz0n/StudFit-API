package com.deiz0n.studfit.controllers.auth;

import com.deiz0n.studfit.domain.dtos.AuthDTO;
import com.deiz0n.studfit.domain.dtos.RecoveryPasswordDTO;
import com.deiz0n.studfit.domain.dtos.ResetPasswordDTO;
import com.deiz0n.studfit.domain.dtos.TokenDTO;
import com.deiz0n.studfit.domain.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletWebRequest;

@Tag(name = "Autenticação", description = "API para autenticação e gerenciamento de senhas do sistema StudFit")
public interface AuthController {

    @Operation(
        summary = "Realizar login",
        description = "Autentica um usuário no sistema e retorna um token de acesso JWT"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso - Token JWT retornado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciais inválidas - E-mail ou senha incorretos",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acesso negado - Usuário sem permissões adequadas",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados de entrada inválidos - Campos obrigatórios não preenchidos, formato de e-mail inválido, ou JSON malformado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha no processo de autenticação ou geração do token",
            content = @Content
        )
    })
    @PostMapping("/login")
    ResponseEntity<Response<?>> entrar(
        @Parameter(description = "Dados de autenticação do usuário (email e senha)", required = true)
        @RequestBody @Valid AuthDTO request,
        ServletWebRequest path
    );

    @Operation(
        summary = "Recuperar senha",
        description = "Envia um código de recuperação de 6 dígitos para o e-mail do usuário"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "E-mail de recuperação enviado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "E-mail não encontrado - Não existe usuário cadastrado com este e-mail",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados de entrada inválidos - E-mail com formato inválido ou campo não preenchido",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha no envio do e-mail de recuperação (problema com servidor SMTP ou configuração de e-mail)",
            content = @Content
        )
    })
    @PostMapping("/recuperar-senha")
    ResponseEntity<Response<?>> recuperarSenha(
        @Parameter(description = "E-mail do usuário para recuperação de senha", required = true)
        @RequestBody @Valid RecoveryPasswordDTO request,
        ServletWebRequest path
    );

    @Operation(
        summary = "Atualizar senha",
        description = "Redefine a senha do usuário utilizando o código de recuperação de 6 dígitos recebido por e-mail"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Senha atualizada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos - Nova senha e confirmação não coincidem, código inválido ou expirado (códigos têm validade de 15 minutos), ou dados de validação inválidos",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Código não encontrado - Código de recuperação não existe, já foi usado ou expirou",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na atualização da senha no banco de dados ou erro inesperado",
            content = @Content
        )
    })
    @PostMapping("/atualizar-senha/")
    ResponseEntity<Response<?>> atualizarSenha(
        @Parameter(description = "Código de recuperação de 6 dígitos recebido por e-mail", required = true, example = "123456")
        @RequestParam String codigo,
        @Parameter(description = "Nova senha e confirmação do usuário", required = true)
        @RequestBody @Valid ResetPasswordDTO request,
        ServletWebRequest path
    );

    @Operation(
        summary = "Validar token",
        description = "Verifica se um token JWT está válido, não expirou e possui assinatura correta"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token válido - Token está ativo e possui assinatura correta",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token inválido - Token expirou, assinatura inválida ou token malformado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados de entrada inválidos - Token não fornecido ou formato inválido",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na verificação do token ou erro inesperado",
            content = @Content
        )
    })
    @PostMapping("/validar-token")
    ResponseEntity<Response<?>> validarToken(
        @Parameter(description = "Token JWT a ser validado", required = true)
        @RequestBody TokenDTO request,
        ServletWebRequest path
    );

}

