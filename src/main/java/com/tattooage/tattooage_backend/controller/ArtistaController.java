package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.PerfilArtista;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.PerfilArtistaRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Artistas", description = "Consulta de artistas y sus perfiles. Acceso público.")
@RestController
@RequestMapping("/api/artistas")
@RequiredArgsConstructor
public class ArtistaController {

    private final UsuarioRepository usuarioRepository;
    private final PerfilArtistaRepository perfilArtistaRepository;

    @Operation(summary = "Listar artistas", description = "Devuelve todos los usuarios con rol ARTISTA.")
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(
                usuarioRepository.findAll().stream()
                        .filter(u -> u.getRol() == Usuario.RolUsuario.ARTISTA)
                        .toList()
        );
    }

    @Operation(summary = "Obtener artista por ID", description = "Devuelve un artista concreto por su ID de usuario.")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .filter(u -> u.getRol() == Usuario.RolUsuario.ARTISTA)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener perfil del artista", description = "Devuelve el perfil detallado (PerfilArtista) asociado al artista.")
    @GetMapping("/{id}/perfil")
    public ResponseEntity<PerfilArtista> getPerfil(@PathVariable Integer id) {
        return perfilArtistaRepository.findByUsuarioIdUsuario(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}