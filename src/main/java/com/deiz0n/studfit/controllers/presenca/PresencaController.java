package com.deiz0n.studfit.controllers.presenca;

import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import com.deiz0n.studfit.domain.response.Response;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Controller de Presença")
public interface PresencaController {

    @GetMapping
    ResponseEntity<Response<?>> buscarPresencas(ServletWebRequest path);

    @Transactional
    @PostMapping("/registrar")
    ResponseEntity<Response<?>> registrar(@RequestBody List<PresencaDTO> request, ServletWebRequest path, @RequestParam @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data);

    @GetMapping("/buscar-por-data")
    ResponseEntity<Response<?>> buscarPorData(ServletWebRequest path, @RequestParam @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data);

}
