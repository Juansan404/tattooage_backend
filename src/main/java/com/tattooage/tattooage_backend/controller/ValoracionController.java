package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Valoracion;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import com.tattooage.tattooage_backend.repository.ValoracionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Valoraciones", description = "Sistema de valoraciones de artistas por clientes")
@RestController
@RequestMapping("/api/valoraciones")
@RequiredArgsConstructor
public class ValoracionController {

    private final ValoracionRepository valoracionRepository;
    private final UsuarioRepository usuarioRepository;

    @Operation(summary = "Valoraciones de un artista ordenadas por fecha")
    @GetMapping("/artista/{idArtista}")
    public ResponseEntity<List<Valoracion>> getByArtista(@PathVariable Integer idArtista) {
        return ResponseEntity.ok(
            valoracionRepository.findByArtistaIdUsuarioOrderByCreadoEnDesc(idArtista)
        );
    }

    @Operation(summary = "Media y total de valoraciones de un artista")
    @GetMapping("/artista/{idArtista}/resumen")
    public ResponseEntity<Map<String, Object>> getResumen(@PathVariable Integer idArtista) {
        Double avg = valoracionRepository.findAvgPuntuacion(idArtista);
        Long total = valoracionRepository.countByArtistaIdUsuario(idArtista);
        double media = avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
        return ResponseEntity.ok(Map.of("media", media, "total", total));
    }

    @Operation(summary = "Valoración de un cliente concreto para un artista")
    @GetMapping("/artista/{idArtista}/cliente/{idCliente}")
    public ResponseEntity<Valoracion> getMia(
            @PathVariable Integer idArtista,
            @PathVariable Integer idCliente) {
        return valoracionRepository
                .findByArtistaIdUsuarioAndClienteIdUsuario(idArtista, idCliente)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear o actualizar valoración (upsert)")
    @PostMapping
    public ResponseEntity<Valoracion> createOrUpdate(@RequestBody Map<String, Object> body) {
        Integer idArtista  = (Integer) body.get("idArtista");
        Integer idCliente  = (Integer) body.get("idCliente");
        Integer puntuacion = (Integer) body.get("puntuacion");
        String  comentario = (String)  body.get("comentario");

        if (idArtista == null || idCliente == null || puntuacion == null
                || puntuacion < 1 || puntuacion > 5) {
            return ResponseEntity.badRequest().build();
        }

        Valoracion val = valoracionRepository
                .findByArtistaIdUsuarioAndClienteIdUsuario(idArtista, idCliente)
                .orElse(new Valoracion());

        val.setArtista(usuarioRepository.getReferenceById(idArtista));
        val.setCliente(usuarioRepository.getReferenceById(idCliente));
        val.setPuntuacion(puntuacion);
        val.setComentario(comentario);

        Valoracion saved = valoracionRepository.save(val);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Eliminar valoración propia")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!valoracionRepository.existsById(id)) return ResponseEntity.notFound().build();
        valoracionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
