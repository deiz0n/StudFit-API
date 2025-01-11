package com.deiz0n.studfit.controller.horario;

import com.deiz0n.studfit.domain.dtos.HorarioDTO;
import com.deiz0n.studfit.domain.enums.Turno;
import com.deiz0n.studfit.domain.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@Tag(name = "Controller do Horário")
public interface HorarioController {

    @GetMapping
    ResponseEntity<Response> getHorarios(ServletWebRequest path);

    @GetMapping("/turno")
    ResponseEntity<Response> getHorariosByTurno(@RequestParam String turno, ServletWebRequest path);

    @PostMapping("/registrar")
    ResponseEntity<Response> createHorario(@RequestBody HorarioDTO request, ServletWebRequest path);

    @DeleteMapping("/excluir/{id}")
    ResponseEntity deleteHorario(@PathVariable UUID id);
}
