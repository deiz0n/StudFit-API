package com.deiz0n.studfit.controllers.horario;

import com.deiz0n.studfit.domain.dtos.HorarioDTO;
import com.deiz0n.studfit.domain.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@Tag(name = "Controller do Horário")
public interface HorarioController {

    @GetMapping
    ResponseEntity<Response<?>> buscarHorarios(ServletWebRequest path);

    @GetMapping("/buscar-por-turno")
    ResponseEntity<Response<?>> buscarPorTurno(@RequestParam String turno, ServletWebRequest path);

    @Transactional
    @PostMapping("/registrar")
    ResponseEntity<Response<?>> registrar(@RequestBody HorarioDTO request, ServletWebRequest path);

    @DeleteMapping("/excluir/{id}")
    ResponseEntity<?> excluir(@PathVariable UUID id);
}
