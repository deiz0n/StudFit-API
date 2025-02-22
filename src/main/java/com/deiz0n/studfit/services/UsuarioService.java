package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.ResetPasswordDTO;
import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.entites.Usuario;
import com.deiz0n.studfit.domain.enums.Cargo;
import com.deiz0n.studfit.domain.events.EnviarEmailRecuperacaoSenhaEvent;
import com.deiz0n.studfit.domain.events.EnviarEmailAlteracaoSenhaEvent;
import com.deiz0n.studfit.domain.events.SolicitarRecuperacaoSenhaEvent;
import com.deiz0n.studfit.domain.events.AtualizarSenhaUsuarioEvent;
import com.deiz0n.studfit.domain.exceptions.usuario.CodigoDeRecuperacaoNotFoundException;
import com.deiz0n.studfit.domain.exceptions.usuario.EmailAlreadyRegisteredException;
import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotExistingException;
import com.deiz0n.studfit.domain.exceptions.usuario.SenhaNotCoincideException;
import com.deiz0n.studfit.domain.exceptions.usuario.UsuarioNotFoundException;
import com.deiz0n.studfit.infrastructure.config.AlgorithmGenerateNumber;
import com.deiz0n.studfit.infrastructure.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UsuarioService(UsuarioRepository repository, ModelMapper mapper, BCryptPasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    public List<UsuarioDTO> buscarHorarios() {
        return repository.findAll()
                .stream()
                .map(usuario -> mapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    public UsuarioDTO registrar(UsuarioDTO usuarioDTO) {
        eExistente(usuarioDTO.getEmail());
        var usuario = mapper.map(usuarioDTO, Usuario.class);

        usuario.setSenha(passwordEncoder.encode(usuario.getPassword()));
        usuario.setCargo(validarCargo(usuarioDTO.getCargo()));

        repository.save(usuario);

        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .cargo(usuario.getCargo().name())
                .build();

    }

    public void excluir(UUID id) {
        var usuario = buscarPorId(id);
        repository.delete(usuario);
    }

    private Usuario buscarPorId(UUID id) {
        return repository.findById(id)
                .map(user -> mapper.map(user, Usuario.class))
                .orElseThrow(
                    () -> new UsuarioNotFoundException(String.format("Usuário com ID: %s não foi encontrado", id.toString()))
                );
    }

    private Cargo validarCargo(String cargo) {
        try {
            return Cargo.valueOf(cargo.toUpperCase());
        } catch (Exception e) {
            throw new ResourceNotExistingException(String.format("Os cargos existentes são: %s", Arrays.toString(Cargo.values())));
        }
    }

    private void eExistente(String email) {
        if (repository.buscarPorEmail(email).isPresent())
            throw new EmailAlreadyRegisteredException("Email já cadastrado");
    }

    @EventListener
    private void geraCodigoRecuperacao(SolicitarRecuperacaoSenhaEvent recoveryPassswordEvent) {
        Usuario usuario = repository.buscarPorEmail(recoveryPassswordEvent.email())
                .orElseThrow(
                        () -> new UsuarioNotFoundException("Usuário não encontrado")
                );

        String codigo = AlgorithmGenerateNumber.generateCode();
        var enviarRecuperacaoSenha = new EnviarEmailRecuperacaoSenhaEvent(new String[]{usuario.getEmail()}, codigo);
        eventPublisher.publishEvent(enviarRecuperacaoSenha);

        usuario.setCodigoRecuperacao(codigo);
        repository.save(usuario);
    }

    @EventListener
    private void atualizaSenha(AtualizarSenhaUsuarioEvent resetPasswordEvent) {
        ResetPasswordDTO newSenha = resetPasswordEvent.resetPassword();
        Usuario usuario = repository.buscarPorCodigoRecuperacao(resetPasswordEvent.codigo())
                .orElseThrow(
                        () -> new CodigoDeRecuperacaoNotFoundException("Código de recuperação não encontrado")
                );

        if (!newSenha.getSenha().equals(newSenha.getConfirmarSenha()))
            throw new SenhaNotCoincideException("As senhas não coincidem");

        usuario.setSenha(passwordEncoder.encode(newSenha.getConfirmarSenha()));
        usuario.setCodigoRecuperacao(null);
        repository.save(usuario);

        var updatedPassword = new EnviarEmailAlteracaoSenhaEvent(new String[]{usuario.getEmail()});
        eventPublisher.publishEvent(updatedPassword);
    }

}
