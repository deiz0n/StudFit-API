package com.deiz0n.studfit.controller.horario;

import com.deiz0n.studfit.domain.dtos.HorarioDTO;
import com.deiz0n.studfit.domain.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.ServletWebRequest;

public interface HorarioController {

    @GetMapping
    ResponseEntity<Response> getHorarios(ServletWebRequest path);
    @PostMapping("/create")
    ResponseEntity<Response> createHorario(@RequestBody HorarioDTO request, ServletWebRequest path);

}
