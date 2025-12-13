package com.deiz0n.studfit.controllers.presenca;

import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import com.deiz0n.studfit.domain.response.Response;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDate;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Presença", description = "API para gerenciamento de presenças dos alunos no sistema StudFit")
public interface PresencaController {

    @Operation(
        summary = "Buscar todas as presenças",
        description = "Retorna uma lista paginada com todas as presenças registradas no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de presenças retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parâmetros de paginação inválidos - Valores negativos para numeroPagina ou quantidade, ou quantidade muito alta (limite máximo)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na consulta ao banco de dados ou erro inesperado",
            content = @Content
        )
    })
    @GetMapping
    ResponseEntity<Response<?>> buscarPresencas(
        ServletWebRequest path,
        @Parameter(description = "Número da página (início em 0)", example = "0")
        @RequestParam(defaultValue = "0") int numeroPagina,
        @Parameter(description = "Quantidade de registros por página (máximo recomendado: 100)", example = "10")
        @RequestParam(defaultValue = "10") int quantidade
    );

    @Operation(
        summary = "Registrar presenças",
        description = "Registra a presença de múltiplos alunos em uma data específica. Cada aluno pode ter apenas uma presença por data."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Presenças registradas com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos - Data futura, aluno não efetivado, ou tentativa de registrar presença para aluno não efetivado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Aluno não encontrado - Um ou mais alunos da lista não existem no sistema",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflito - Já existe presença registrada para um ou mais alunos na data especificada",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "422",
            description = "Data inválida - Formato de data inválido ou data futura não permitida",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na persistência dos dados ou erro inesperado",
            content = @Content
        )
    })
    @Transactional
    @PostMapping("/registrar")
    ResponseEntity<Response<?>> registrar(
        @Parameter(description = "Lista de presenças a serem registradas (IDs dos alunos e status de presença)", required = true)
        @RequestBody List<PresencaDTO> request,
        ServletWebRequest path,
        @Parameter(description = "Data das presenças no formato dd/MM/yyyy. Não é possível registrar presenças em datas futuras", required = true, example = "15/03/2024")
        @RequestParam @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data
    );

    @Operation(
        summary = "Buscar presenças por data",
        description = "Retorna uma lista paginada de presenças filtrada por uma data específica, útil para controle diário de frequência"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Presenças da data retornadas com sucesso - Lista pode estar vazia se não houver presenças registradas na data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parâmetros inválidos - Formato de data inválido (deve ser dd/MM/yyyy) ou parâmetros de paginação inválidos (valores negativos)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "422",
            description = "Data inválida - Data com formato incorreto ou data que não pode ser processada",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na consulta ao banco de dados",
            content = @Content
        )
    })
    @GetMapping("/buscar-por-data/")
    ResponseEntity<Response<?>> buscarPorData(
        ServletWebRequest path,
        @Parameter(description = "Data para filtrar as presenças no formato dd/MM/yyyy", required = true, example = "15/03/2024")
        @RequestParam @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data,
        @Parameter(description = "Número da página (início em 0)", example = "0")
        @RequestParam(defaultValue = "0") int numeroPagina,
        @Parameter(description = "Quantidade de registros por página", example = "10")
        @RequestParam(defaultValue = "10") int quantidade
    );

}
