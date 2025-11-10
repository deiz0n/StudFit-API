package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.ResetPasswordDTO;
import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.entites.Usuario;
import com.deiz0n.studfit.domain.enums.Cargo;
import com.deiz0n.studfit.domain.events.AtualizarSenhaUsuarioEvent;
import com.deiz0n.studfit.domain.events.EnviarEmailAlteracaoSenhaEvent;
import com.deiz0n.studfit.domain.events.EnviarEmailRecuperacaoSenhaEvent;
import com.deiz0n.studfit.domain.events.SolicitarRecuperacaoSenhaEvent;
import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotExistingException;
import com.deiz0n.studfit.domain.exceptions.usuario.CodigoDeRecuperacaoNotFoundException;
import com.deiz0n.studfit.domain.exceptions.usuario.EmailAlreadyRegisteredException;
import com.deiz0n.studfit.domain.exceptions.usuario.SenhaNotCoincideException;
import com.deiz0n.studfit.domain.exceptions.usuario.UsuarioNotFoundException;
import com.deiz0n.studfit.infrastructure.config.AlgorithmGenerateNumber;
import com.deiz0n.studfit.infrastructure.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private static final UUID ID = UUID.fromString("4da359b4-7b4b-4d84-98f8-617d953ce90c");
    private static final String EMAIL = "email@teste.com";
    private static final String CARGO = Cargo.ADMINISTRADOR.name();

    @Mock
    UsuarioRepository repository;
    @Mock
    ModelMapper mapper;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    UsuarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser();
        mockUserDTO();
    }

    @Test
    void whenBuscarUsuariosThenReturnListOfUsuarioDTO() {
        var usuario = mockUser();
        var usuarioDTO = mockUserDTO();

        when(passwordEncoder.encode(anyString())).thenReturn("senha criptografada");
        when(repository.findAll()).thenReturn(List.of(usuario));
        when(mapper.map(usuario, UsuarioDTO.class)).thenReturn(usuarioDTO);

        var result = service.buscarUsuarios();

        verify(repository).findAll();
        verify(mapper).map(usuario, UsuarioDTO.class);

        assertNotNull(result.get(0));
        assertEquals(1, result.size());
        assertEquals(ID.toString(), result.get(0).getId());
        assertEquals(EMAIL, result.get(0).getEmail());
        assertEquals(CARGO, result.get(0).getCargo());
    }

    @Test
    void whenRegistrarThenReturnUsuarioDTO() {
        var usuario = mockUser();
        var usuarioDTO = mockUserDTO();

        when(passwordEncoder.encode(anyString())).thenReturn("senha criptografada");
        when(mapper.map(usuarioDTO, Usuario.class)).thenReturn(usuario);
        when(repository.save(usuario)).thenReturn(usuario);

        var result = service.registrar(usuarioDTO);

        verify(repository).save(usuario);
        verify(mapper).map(usuarioDTO, Usuario.class);

        assertNotNull(result.getId());
        assertEquals(ID, result.getId());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(CARGO, result.getCargo());
    }

    @Test
    void whenRegistrarThenThrowEmailAlreadyRegisteredException() {
        var usuario = mockUser();
        var usuarioDTO = mockUserDTO();

        when(repository.buscarPorEmail(anyString())).thenReturn(Optional.of(usuario));

        EmailAlreadyRegisteredException exception = assertThrows(
                EmailAlreadyRegisteredException.class,
                () -> service.registrar(usuarioDTO)
        );

        assertEquals("Email já cadastrado", exception.getMessage());

        verify(repository).buscarPorEmail(EMAIL);
        verify(repository, never()).save(usuario);
    }

    @Test
    void whenRegistrarThenThrowResourceNotExistingException() {
        var usuario = mockUser();
        var usuarioDTO = mockUserDTO();

        usuarioDTO.setCargo("Algum cargo");

        when(repository.buscarPorEmail(anyString())).thenReturn(Optional.empty());
        when(mapper.map(usuarioDTO, Usuario.class)).thenReturn(usuario);

        ResourceNotExistingException exception = assertThrows(
                ResourceNotExistingException.class,
                () -> service.registrar(usuarioDTO)
        );

        assertEquals(
                "Os cargos existentes são: [ADMINISTRADOR, INSTRUTOR, ESTAGIARIO]",
                exception.getMessage()
        );

        verify(repository, never()).save(usuario);
    }

    @Test
    void whenExcluirThenCallRepositoryDelete() {
        var usuario = mockUser();

        when(repository.findById(ID)).thenReturn(Optional.of(usuario));
        when(mapper.map(usuario, Usuario.class)).thenReturn(usuario);

        service.excluir(ID);

        verify(repository).findById(ID);
        verify(repository).delete(usuario);
        verify(repository, times(1)).delete(usuario);
    }

    @Test
    void whenGerarCodigoRecuperacaoThenUpdateUsuarioAndPublishEvent() {
        var evento = new SolicitarRecuperacaoSenhaEvent(EMAIL);
        var usuario = mockUser();
        ArgumentCaptor<EnviarEmailRecuperacaoSenhaEvent> eventCaptor =
                ArgumentCaptor.forClass(EnviarEmailRecuperacaoSenhaEvent.class);

        when(repository.buscarPorEmail(anyString())).thenReturn(Optional.of(usuario));

        service.geraCodigoRecuperacao(evento);

        verify(repository).buscarPorEmail(EMAIL);
        verify(repository).save(usuario);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        EnviarEmailRecuperacaoSenhaEvent capturedEvent = eventCaptor.getValue();

        assertArrayEquals(new String[]{EMAIL}, capturedEvent.destinatario());
        assertNotNull(capturedEvent.codigo());
        assertEquals(usuario.getCodigoRecuperacao(), capturedEvent.codigo());
    }

    @Test
    void whenGerarCodigoRecuperacaoThenThrowUsuarioNotFoundException() {
        var evento = new SolicitarRecuperacaoSenhaEvent(EMAIL);
        var usuario = mockUser();

        when(repository.buscarPorEmail(anyString())).thenReturn(Optional.empty());

        UsuarioNotFoundException exception = assertThrows(
                UsuarioNotFoundException.class,
                () -> service.geraCodigoRecuperacao(evento)
        );

        assertEquals(
                "Usuário não encontrado",
                exception.getMessage()
        );

        verify(repository, never()).save(usuario);
        verify(eventPublisher, never()).publishEvent(any(EnviarEmailRecuperacaoSenhaEvent.class));
    }

    @Test
    void whenAtualizaSenhaThenUpdatePassword() {
        var usuario = mockUser();
        var codigoRecuperacao = "B69AN1";
        var resetPasswordDTO = new ResetPasswordDTO("novaSenha", "novaSenha");
        var evento = new AtualizarSenhaUsuarioEvent(codigoRecuperacao, resetPasswordDTO);
        ArgumentCaptor<EnviarEmailAlteracaoSenhaEvent> eventCaptor =
                ArgumentCaptor.forClass(EnviarEmailAlteracaoSenhaEvent.class);

        when(repository.buscarPorCodigoRecuperacao(anyString())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");

        service.atualizaSenha(evento);

        verify(repository).buscarPorCodigoRecuperacao(codigoRecuperacao);
        verify(repository).save(usuario);
        verify(passwordEncoder).encode("novaSenha");
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        EnviarEmailAlteracaoSenhaEvent capturedEvent = eventCaptor.getValue();

        assertArrayEquals(new String[]{EMAIL}, capturedEvent.destinatario());
        assertEquals("senhaCriptografada", usuario.getSenha());
        assertNull(usuario.getCodigoRecuperacao());
    }

    @Test
    void whenAtualizaSenhaThenThrowCodigoDeRecuperacaoNotFoundException() {
        var usuario = mockUser();
        var codigoRecuperacao = "B69AN1";
        var resetPasswordDTO = new ResetPasswordDTO("novaSenha", "novaSenha");
        var atualizarSenhaEvent = new AtualizarSenhaUsuarioEvent(codigoRecuperacao, resetPasswordDTO);

        when(repository.buscarPorCodigoRecuperacao(anyString())).thenReturn(Optional.empty());

        CodigoDeRecuperacaoNotFoundException exception = assertThrows(
                CodigoDeRecuperacaoNotFoundException.class,
                () -> service.atualizaSenha(atualizarSenhaEvent)
        );

        assertEquals(
                "Código de recuperação não encontrado",
                exception.getMessage()
        );

        verify(passwordEncoder, never()).encode("novaSenha");
        verify(repository, never()).save(usuario);
        verify(eventPublisher, never()).publishEvent(any(EnviarEmailRecuperacaoSenhaEvent.class));
    }

    @Test
    void whenAtualizaSenhaThenThrowSenhaNotCoincideException() {
        var usuario = mockUser();
        var codigoRecuperacao = "B69AN1";
        var resetPasswordDTO = new ResetPasswordDTO("novaSenha", "senhaDiferente");
        var atualizarSenhaEvent = new AtualizarSenhaUsuarioEvent(codigoRecuperacao, resetPasswordDTO);

        when(repository.buscarPorCodigoRecuperacao(anyString())).thenReturn(Optional.of(usuario));

        SenhaNotCoincideException exception = assertThrows(
                SenhaNotCoincideException.class,
                () -> service.atualizaSenha(atualizarSenhaEvent)
        );

        assertEquals(
                "As senhas não coincidem",
                exception.getMessage()
        );

        verify(passwordEncoder, never()).encode("novaSenha");
        verify(repository, never()).save(usuario);
        verify(eventPublisher, never()).publishEvent(any(EnviarEmailRecuperacaoSenhaEvent.class));
    }

    Usuario mockUser() {
        return new Usuario(
                ID,
                "Usuario Teste",
                EMAIL,
                passwordEncoder.encode("123"),
                AlgorithmGenerateNumber.generateCode(),
                Cargo.valueOf(CARGO),
                Instant.now(),
                Instant.now(),
                List.of()
        );
    }

    UsuarioDTO mockUserDTO() {
        return UsuarioDTO.builder()
                .id(ID)
                .nome("Usuário teste")
                .email(EMAIL)
                .cargo(CARGO)
                .senha(passwordEncoder.encode("123"))
                .createdAt(Instant.now())
                .build();

    }

}