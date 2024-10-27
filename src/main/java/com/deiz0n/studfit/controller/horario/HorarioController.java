package com.deiz0n.studfit.controller.horario;

import com.deiz0n.studfit.domain.dtos.HorarioDTO;
import com.deiz0n.studfit.domain.enums.Turno;
import com.deiz0n.studfit.domain.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

public interface HorarioController {

    @GetMapping
    ResponseEntity<Response> getHorarios(ServletWebRequest path);
    @GetMapping("/")
    ResponseEntity<Response> getHorariosByTurno(@RequestParam String turno, ServletWebRequest path);
    @PostMapping("/create")
    ResponseEntity<Response> createHorario(@RequestBody HorarioDTO request, ServletWebRequest path);
    @DeleteMapping("/delete/{id}")
    ResponseEntity deleteHorario(@PathVariable UUID id);
}
