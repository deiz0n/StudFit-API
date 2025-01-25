package com.deiz0n.studfit.controller.presenca;

import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import com.deiz0n.studfit.domain.response.Response;
import com.deiz0n.studfit.services.PresencaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1.0/presencas")
public class PresencaControllerImpl implements PresencaController {

    private PresencaService service;

    public PresencaControllerImpl(PresencaService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Response> buscarPresencas(ServletWebRequest path) {
        var presencas = service.buscarPresencas();
        return ResponseEntity.ok()
                .body(Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .path(path.getRequest().getRequestURI())
                .data(presencas)
                .build());
    }

    @Override
    public ResponseEntity<Response> registrar(List<PresencaDTO> request, ServletWebRequest path, LocalDate data) {
        var presenca = service.registar(request, data);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("id")
                .buildAndExpand()
                .toUri();
        return ResponseEntity.created(uri)
                .body(Response.builder()
                        .code(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .path(path.getRequest().getRequestURI())
                        .data(presenca)
                        .build());
    }

    @Override
    public ResponseEntity<Response> buscarPorData(ServletWebRequest path, LocalDate data) {
        var presencas = service.buscarPorData(data);
        return ResponseEntity.ok()
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(presencas)
                        .build());
    }
}
