package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.PerfilArtista;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.PerfilArtistaRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artistas")
@RequiredArgsConstructor
public class ArtistaController {

    private final UsuarioRepository usuarioRepository;
    private final PerfilArtistaRepository perfilArtistaRepository;

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(
                usuarioRepository.findAll().stream()
                        .filter(u -> u.getRol() == Usuario.RolUsuario.ARTISTA)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .filter(u -> u.getRol() == Usuario.RolUsuario.ARTISTA)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/perfil")
    public ResponseEntity<PerfilArtista> getPerfil(@PathVariable Integer id) {
        return perfilArtistaRepository.findByUsuarioIdUsuario(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}