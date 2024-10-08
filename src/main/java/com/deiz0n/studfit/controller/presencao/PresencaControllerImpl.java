package com.deiz0n.studfit.controller.presencao;

import com.deiz0n.studfit.domain.response.Response;
import com.deiz0n.studfit.services.PresencaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

@RestController
@RequestMapping("api/v1.0/presencas")
public class PresencaControllerImpl implements PresencaController {

    private PresencaService service;

    public PresencaControllerImpl(PresencaService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Response> getAll(ServletWebRequest path) {
        var presencas = service.getAll();
        return ResponseEntity.ok()
                .body(Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .path(path.getRequest().getRequestURI())
                .data(presencas)
                .build());
    }
}
