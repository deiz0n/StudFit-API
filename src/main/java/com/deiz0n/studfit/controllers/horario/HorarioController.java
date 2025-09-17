package com.deiz0n.studfit.controllers.horario;

import com.deiz0n.studfit.domain.dtos.HorarioDTO;
import com.deiz0n.studfit.domain.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@Tag(name = "Horário", description = "API para gerenciamento de horários do sistema StudFit")
public interface HorarioController {

    @Operation(
        summary = "Buscar todos os horários",
        description = "Retorna uma lista com todos os horários cadastrados no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de horários retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na consulta ao banco de dados ou erro inesperado",
            content = @Content
        )
    })
    @GetMapping
    ResponseEntity<Response<?>> buscarHorarios(ServletWebRequest path);

    @Operation(
        summary = "Buscar horários por turno",
        description = "Retorna os horários filtrados por um turno específico (MANHA, TARDE, NOITE)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Horários do turno retornados com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Turno inválido - Turno informado não existe. Valores válidos: MANHA, TARDE, NOITE",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na consulta ao banco de dados",
            content = @Content
        )
    })
    @GetMapping("/buscar-por-turno/")
    ResponseEntity<Response<?>> buscarPorTurno(
        @Parameter(description = "Turno para filtrar os horários. Valores válidos: MANHA, TARDE, NOITE", required = true, example = "MANHA")
        @RequestParam String turno,
        ServletWebRequest path
    );

    @Operation(
        summary = "Registrar novo horário",
        description = "Cria um novo horário no sistema com os dados fornecidos (horário de início, fim e turno)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Horário criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos - Horário de início posterior ao fim, horários sobrepostos, formato de hora inválido, ou já existe um horário cadastrado no mesmo período",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflito - Já existe um horário cadastrado que conflita com o período informado",
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
        @Parameter(description = "Dados do horário a ser criado (horário de início, fim e turno)", required = true)
        @RequestBody HorarioDTO request,
        ServletWebRequest path
    );

    @Operation(
        summary = "Excluir horário",
        description = "Remove um horário do sistema pelo seu ID. Atenção: esta ação não pode ser desfeita"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Horário excluído com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Horário não encontrado - O horário com o ID especificado não existe no sistema",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID inválido - Formato de UUID inválido ou parâmetro malformado, ou não é possível excluir horário que possui alunos vinculados",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflito - Horário não pode ser excluído pois possui alunos matriculados neste período",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor - Falha na exclusão dos dados ou erro inesperado",
            content = @Content
        )
    })
    @DeleteMapping("/excluir/{id}")
    ResponseEntity<?> excluir(
        @Parameter(description = "ID único do horário a ser excluído", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID id
    );
}
