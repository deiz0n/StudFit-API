package com.deiz0n.studfit.controllers.presenca;

import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import com.deiz0n.studfit.domain.response.Response;
import com.deiz0n.studfit.services.PresencaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1.0/presencas")
public class PresencaControllerImpl implements PresencaController {

    private final PresencaService service;

    public PresencaControllerImpl(PresencaService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Response<?>> buscarPresencas(ServletWebRequest path, @RequestParam(defaultValue = "0") int numeroPagina, @RequestParam(defaultValue = "10") int quantidade) {
        var presencas = service.buscarPresencas(numeroPagina, quantidade);
        return ResponseEntity.ok()
                .body(Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .path(path.getRequest().getRequestURI())
                .data(presencas)
                .build());
    }

    @Override
    public ResponseEntity<Response<?>> registrar(List<PresencaDTO> request, ServletWebRequest path, LocalDate data) {
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
    public ResponseEntity<Response<?>> buscarPorData(ServletWebRequest path, LocalDate data, @RequestParam(defaultValue = "0") int numeroPagina, @RequestParam(defaultValue = "10") int quantidade) {
        var presencas = service.buscarPorData(data, numeroPagina, quantidade);
        return ResponseEntity.ok()
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(presencas)
                        .build());
    }
}
