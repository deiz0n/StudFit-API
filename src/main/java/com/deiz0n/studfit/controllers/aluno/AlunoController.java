package com.deiz0n.studfit.controllers.aluno;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.domain.dtos.AtualizarAlunoDTO;
import com.deiz0n.studfit.domain.enums.Status;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Aluno", description = "API para gerenciamento de alunos do sistema StudFit")
public interface AlunoController {

    @Operation(
        summary = "Buscar aluno por ID",
        description = "Retorna os dados de um aluno específico pelo seu identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Aluno encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Aluno não encontrado - O aluno com o ID especificado não existe no sistema",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID inválido - Formato de UUID inválido ou parâmetro malformado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na consulta ao banco de dados ou erro inesperado",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<Response<?>> buscarAluno(
        @Parameter(description = "ID único do aluno", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID id,
        ServletWebRequest path
    );

    @Operation(
        summary = "Buscar alunos na lista de espera",
        description = "Retorna uma lista paginada de alunos aguardando efetivação, filtrada por turno",
        security = {}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de alunos retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parâmetros inválidos - Turno informado não existe (valores válidos: MANHA, TARDE, NOITE) ou parâmetros de paginação inválidos",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na consulta ao banco de dados",
            content = @Content
        )
    })
    @GetMapping("lista-espera")
    ResponseEntity<Response<?>> buscarAlunosListaEspera(
            ServletWebRequest path,
            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0") int numeroPagina,
            @Parameter(description = "Quantidade de registros por página", example = "10")
            @RequestParam(defaultValue = "10") int quantidade,
            @Parameter(description = "Turno dos alunos", required = true, example = "MANHA")
            @RequestParam String turno
    );

    @Operation(
        summary = "Registrar aluno na lista de espera",
        description = "Adiciona um novo aluno à lista de espera para efetivação posterior",
        security = {}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Aluno registrado na lista de espera com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos - E-mail já cadastrado, telefone já cadastrado, ou dados de validação inválidos (campos obrigatórios, formato de e-mail, etc.)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha no envio de e-mail de confirmação ou erro na persistência dos dados",
            content = @Content
        )
    })
    @Transactional
    @PostMapping("/lista-espera/registrar")
    ResponseEntity<Response<?>> registrarAlunoListaEspera(
        @Parameter(description = "Dados do aluno para lista de espera", required = true)
        @RequestBody @Valid AlunoListaEsperaDTO request,
        ServletWebRequest path
    );

    @Operation(
        summary = "Excluir aluno da lista de espera",
        description = "Remove um aluno da lista de espera pelo seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Aluno removido da lista de espera com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Aluno não encontrado - O aluno com o ID especificado não existe na lista de espera",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID inválido - Formato de UUID inválido",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na exclusão dos dados ou erro inesperado",
            content = @Content
        )
    })
    @Transactional
    @DeleteMapping("/lista-espera/excluir/{id}")
    ResponseEntity<?> excluirAlunoListaEspera(
        @Parameter(description = "ID único do aluno", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID id,
        ServletWebRequest path
    );

    @Operation(
        summary = "Buscar alunos efetivados",
        description = "Retorna uma lista paginada de alunos já efetivados no sistema, com filtros opcionais",
        security = {}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de alunos efetivados retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parâmetros inválidos - Turno informado não existe, Status inválido, ou parâmetros de paginação inválidos (numeroPagina/quantidade negativos)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na consulta ao banco de dados",
            content = @Content
        )
    })
    @GetMapping("efetivados")
    ResponseEntity<Response<?>> buscarAlunosEfetivados(
            ServletWebRequest path,
            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0", required = false) int numeroPagina,
            @Parameter(description = "Quantidade de registros por página", example = "100")
            @RequestParam(defaultValue = "100", required = false) int quantidade,
            @Parameter(description = "Filtro por turno", example = "MANHA")
            @RequestParam(required = false) String turno,
            @Parameter(description = "Filtro por status do aluno")
            @RequestParam(required = false) Status status
    );

    @Operation(
        summary = "Efetivar aluno (Descontinuado)",
        description = "Efetiva um aluno da lista de espera. Este endpoint está marcado como descontinuado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Aluno efetivado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos - E-mail já cadastrado por outro aluno, telefone já cadastrado, ou dados de validação inválidos",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Aluno não encontrado - O aluno com o ID especificado não existe na lista de espera",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha no envio de e-mail de confirmação ou erro na persistência dos dados",
            content = @Content
        )
    })
    @Deprecated
    @Transactional
    @PatchMapping("efetivados/efetivar/{id}")
    ResponseEntity<?> registrarAlunoEfetivado(
        @Parameter(description = "Dados do aluno para efetivação", required = true)
        @RequestBody @Valid AlunoDTO request,
        @Parameter(description = "ID único do aluno", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID id,
        ServletWebRequest path
    );

    @Operation(
        summary = "Excluir aluno efetivado",
        description = "Remove permanentemente um aluno efetivado do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Aluno efetivado excluído com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Aluno não encontrado - O aluno com o ID especificado não existe ou não está efetivado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID inválido - Formato de UUID inválido",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na exclusão dos dados ou erro inesperado",
            content = @Content
        )
    })
    @Transactional
    @DeleteMapping("efetivados/excluir/{id}")
    ResponseEntity<?> excluirAlunoEfetivado(
        @Parameter(description = "ID único do aluno", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID id
    );

    @Operation(
        summary = "Atualizar dados do aluno efetivado",
        description = "Atualiza as informações de um aluno já efetivado no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Aluno atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos - E-mail já cadastrado por outro aluno, telefone já cadastrado, ou dados de validação inválidos (campos obrigatórios, formato de e-mail, etc.)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Aluno não encontrado - O aluno com o ID especificado não existe ou não está efetivado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na atualização dos dados ou erro inesperado",
            content = @Content
        )
    })
    @Transactional
    @PatchMapping("efetivado/atualizar/{id}")
    ResponseEntity<Response<?>> atualizarAlunoEfetivado(
        @Parameter(description = "ID único do aluno", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID id,
        @Parameter(description = "Dados atualizados do aluno", required = true)
        @RequestBody @Valid AtualizarAlunoDTO request,
        ServletWebRequest path
    );

    @Operation(
        summary = "Adicionar atestado médico",
        description = "Anexa um atestado médico ao perfil do aluno efetivado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Atestado adicionado com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Arquivo ou dados inválidos - Arquivo não é um PDF válido, arquivo muito grande (limite: 10MB), nome de arquivo inválido, ou aluno não está efetivado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Aluno não encontrado - O aluno com o ID especificado não existe",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "413",
            description = "Arquivo muito grande - O arquivo excede o tamanho máximo permitido de 10MB",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na criação do diretório de armazenamento, falha no salvamento do arquivo, ou erro inesperado",
            content = @Content
        )
    })
    @Transactional
    @PostMapping("efetivados/adicionar-atestado/{id}")
    ResponseEntity<Void> adicionarAtestado(
        @Parameter(description = "ID único do aluno", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID id,
        @Parameter(description = "Arquivo do atestado médico em formato PDF (máximo 10MB)", required = true)
        @RequestParam("atestado") MultipartFile atestado
    );
}
