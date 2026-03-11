package com.tattooage.tattooage_backend.service;

import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> getArtistas() {
        return usuarioRepository.findByRolAndActivoTrue(Usuario.RolUsuario.ARTISTA);
    }

    public Usuario getUsuarioById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario actualizarUsuario(Integer id, Usuario datos) {
        Usuario usuario = getUsuarioById(id);
        if (datos.getNombre() != null) usuario.setNombre(datos.getNombre());
        if (datos.getApellidos() != null) usuario.setApellidos(datos.getApellidos());
        if (datos.getTelefono() != null) usuario.setTelefono(datos.getTelefono());
        if (datos.getBio() != null) usuario.setBio(datos.getBio());
        if (datos.getAvatar() != null) usuario.setAvatar(datos.getAvatar());
        return usuarioRepository.save(usuario);
    }
}