package com.deiz0n.studfit.unit.controllers;

import com.deiz0n.studfit.controllers.usuario.UsuarioController;
import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.enums.Cargo;
import com.deiz0n.studfit.infrastructure.repositories.UsuarioRepository;
import com.deiz0n.studfit.infrastructure.security.TokenService;
import com.deiz0n.studfit.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerImplTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String BASE_URL = "/api/v1.0/usuarios";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private TokenService tokenService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMINISTRADOR")
    void whenBuscarUsuariosThenReturnHttpOk() throws Exception {
        when(usuarioService.buscarUsuarios()).thenReturn(mockUsuarios());

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].nome").value("nome"));

        verify(usuarioService, times(1)).buscarUsuarios();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMINISTRADOR")
    void whenRegistrarThenReturnHttpCreated() throws Exception {
        var usuarioResponse = mockUsuario();
        when(usuarioService.registrar(any(UsuarioDTO.class))).thenReturn(usuarioResponse);

        var usuarioRequest = UsuarioDTO.builder()
                .nome("nome")
                .email("email@test.com")
                .senha("senha")
                .cargo(Cargo.ESTAGIARIO.name())
                .build();

        mockMvc.perform(post(BASE_URL + "/registrar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonWithoutReadOnly(usuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201 ))
                .andExpect(jsonPath("$.data.nome").value("nome"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.created_at").exists())
                .andExpect(jsonPath("$.data.updated_at").exists());

        verify(usuarioService, times(1)).registrar(any(UsuarioDTO.class));
    }



    @Test
    @WithMockUser(username = "admin", roles = {"INSTRUTOR"})
    void whenExcluirThenReturnNoContent() throws Exception {
        doNothing().when(usuarioService).excluir(ID);

        mockMvc.perform(delete(BASE_URL + "/excluir/{id}", ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).excluir(ID);
    }

    private UsuarioDTO mockUsuario() {
        return UsuarioDTO.builder()
                .id(ID)
                .nome("nome")
                .email("email@test.com")
                .senha("senha")
                .cargo(Cargo.ESTAGIARIO.name())
                .updatedAt(Instant.now())
                .createdAt(Instant.now())
                .build();
    }

    private List<UsuarioDTO> mockUsuarios() {
        return List.of(mockUsuario());
    }

    private String toJsonWithoutReadOnly(UsuarioDTO dto) throws Exception {
        var node = objectMapper.createObjectNode();
        node.put("nome", dto.getNome());
        node.put("email", dto.getEmail());
        node.put("senha", dto.getSenha());
        node.put("cargo", dto.getCargo());
        return objectMapper.writeValueAsString(node);
    }
}