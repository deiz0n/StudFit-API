package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.entites.Usuario;
import com.deiz0n.studfit.domain.exceptions.UsuarioNotFoundException;
import com.deiz0n.studfit.infrastructure.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private UsuarioRepository repository;
    private ModelMapper mapper;
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, ModelMapper mapper, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(usuario -> mapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        var usuario = mapper.map(usuarioDTO, Usuario.class);
        usuario.setSenha(passwordEncoder.encode(usuario.getPassword()));
        repository.save(usuario);

        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .cargo(usuario.getCargo())
                .build();
    }

    public void delete(UUID id) {
        var usuario = findByID(id);
        repository.delete(usuario);
    }

    private Usuario findByID(UUID id) {
        return repository.findById(id)
                .map(user -> mapper.map(user, Usuario.class))
                .orElseThrow(
                    () -> new UsuarioNotFoundException(String.format("Usuário com ID: %s não foi encontrado", id.toString()))
                );
    }

}
