package com.deiz0n.studfit.controller.presenca;

import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import com.deiz0n.studfit.domain.response.Response;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDate;
import java.util.List;

public interface PresencaController {

    @GetMapping
    ResponseEntity<Response> getPresencas(ServletWebRequest path);
    @PostMapping("/create")
    ResponseEntity<Response> createPresenca(@RequestBody List<PresencaDTO> request, ServletWebRequest path, @RequestParam @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data);
    @GetMapping("/")
    ResponseEntity<Response> getPresencasByData(ServletWebRequest path, @RequestParam @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data);

}
