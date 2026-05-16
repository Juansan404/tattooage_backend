package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Estilo;
import com.tattooage.tattooage_backend.entity.PerfilArtista;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.EstiloRepository;
import com.tattooage.tattooage_backend.repository.PerfilArtistaRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Estilos", description = "Catálogo de estilos de tatuaje")
@RestController
@RequestMapping("/api/estilos")
@RequiredArgsConstructor
public class EstiloController {

    private final EstiloRepository estiloRepository;
    private final PerfilArtistaRepository perfilArtistaRepository;
    private final UsuarioRepository usuarioRepository;

    @Operation(summary = "Listar todos los estilos ordenados por popularidad (likes totales)")
    @GetMapping
    public ResponseEntity<List<Estilo>> getAll() {
        return ResponseEntity.ok(estiloRepository.findAllOrderByLikesDesc());
    }

    @Operation(summary = "Estilos preferidos de un artista")
    @GetMapping("/artista/{idUsuario}")
    public ResponseEntity<List<Estilo>> getByArtista(@PathVariable Integer idUsuario) {
        return perfilArtistaRepository.findByUsuarioIdUsuario(idUsuario)
                .map(p -> ResponseEntity.ok(p.getEstilos()))
                .orElse(ResponseEntity.ok(List.of()));
    }

    @Operation(summary = "Actualizar estilos preferidos de un artista")
    @PutMapping("/artista/{idUsuario}")
    @Transactional
    public ResponseEntity<List<Estilo>> updateArtista(
            @PathVariable Integer idUsuario,
            @RequestBody List<String> nombres) {

        PerfilArtista perfil = perfilArtistaRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseGet(() -> {
                    Usuario u = usuarioRepository.findById(idUsuario).orElseThrow();
                    PerfilArtista p = new PerfilArtista();
                    p.setUsuario(u);
                    return p;
                });
        List<Estilo> estilos = nombres.stream()
                .filter(n -> n != null && !n.isBlank())
                .map(n -> estiloRepository.findByNombreIgnoreCase(n.trim())
                        .orElseGet(() -> estiloRepository.save(
                                Estilo.builder().nombre(n.trim().toLowerCase()).build())))
                .collect(Collectors.toList());
        perfil.setEstilos(estilos);
        perfilArtistaRepository.save(perfil);
        return ResponseEntity.ok(estilos);
    }
}
