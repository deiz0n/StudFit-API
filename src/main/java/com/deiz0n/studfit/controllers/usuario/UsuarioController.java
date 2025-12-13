package com.deiz0n.studfit.controllers.usuario;

import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuário", description = "API para gerenciamento de usuários do sistema StudFit")
public interface UsuarioController {

    @Operation(
        summary = "Buscar todos os usuários",
        description = "Retorna uma lista com todos os usuários cadastrados no sistema (administradores e funcionários)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acesso negado - Usuário sem permissões para visualizar lista de usuários (apenas administradores)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Não autenticado - Token JWT inválido, expirado ou não fornecido",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na consulta ao banco de dados ou erro inesperado",
            content = @Content
        )
    })
    @GetMapping
    ResponseEntity<Response<?>> buscarUsuarios(ServletWebRequest path);

    @Operation(
        summary = "Registrar novo usuário",
        description = "Cria um novo usuário no sistema com cargo de administrador ou funcionário"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso - E-mail de boas-vindas enviado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos - E-mail já cadastrado, telefone já cadastrado, cargo informado não existe (valores válidos: [ADMINISTRADOR, ESTAGIARIO, INSTRUTOR]) ou dados de validação inválidos (campos obrigatórios, formato de e-mail, etc.)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acesso negado - Usuário sem permissões para criar usuários (apenas administradores)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Não autenticado - Token JWT inválido, expirado ou não fornecido",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha no envio do e-mail de boas-vindas (problema com servidor SMTP), ou erro na persistência dos dados",
            content = @Content
        )
    })
    @Transactional
    @PostMapping("/registrar")
    ResponseEntity<Response<?>> registrar(
        @Parameter(description = "Dados do usuário a ser criado (nome, email, telefone, cargo)", required = true)
        @RequestBody @Valid UsuarioDTO request,
        ServletWebRequest path
    );

    @Operation(
        summary = "Excluir usuário",
        description = "Remove um usuário do sistema pelo seu ID. Atenção: esta ação não pode ser desfeita e o usuário perderá acesso imediatamente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Usuário excluído com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado - O usuário com o ID especificado não existe no sistema",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID inválido - Formato de UUID inválido ou tentativa de auto-exclusão (usuário não pode excluir a si mesmo)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acesso negado - Usuário sem permissões para excluir usuários (apenas administradores), ou tentativa de excluir outro administrador",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Não autenticado - Token JWT inválido, expirado ou não fornecido",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflito - Não é possível excluir o último administrador do sistema ou usuário com dependências ativas",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na exclusão dos dados ou erro inesperado",
            content = @Content
        )
    })
    @Transactional
    @DeleteMapping("/excluir/{id}")
    ResponseEntity<?> excluir(
        @Parameter(description = "ID único do usuário a ser excluído", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID id
    );

}
