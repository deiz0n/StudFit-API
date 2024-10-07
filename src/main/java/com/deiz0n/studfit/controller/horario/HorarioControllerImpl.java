package com.deiz0n.studfit.controller.horario;

import com.deiz0n.studfit.domain.response.Response;
import com.deiz0n.studfit.services.HorarioService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/horarios")
public class HorarioControllerImpl  implements HorarioController{

    private HorarioService service;

    public HorarioControllerImpl(HorarioService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Response> getHorarios(ServletWebRequest path) {
        var horarios = service.getAll();
        return ResponseEntity.ok()
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(horarios)
                        .build());
    }
}
