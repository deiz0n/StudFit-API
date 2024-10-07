package com.deiz0n.studfit.controller.horario;

import com.deiz0n.studfit.domain.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

public interface HorarioController {

    @GetMapping
    ResponseEntity<Response> getHorarios(ServletWebRequest path);

}
