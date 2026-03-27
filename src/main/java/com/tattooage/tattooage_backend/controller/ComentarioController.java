package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Comentario;
import com.tattooage.tattooage_backend.repository.ComentarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comentarios", description = "Gestión de comentarios en publicaciones. Requiere autenticación.")
@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioRepository comentarioRepository;

    @Operation(summary = "Listar todos los comentarios")
    @GetMapping
    public ResponseEntity<List<Comentario>> getAll() {
        return ResponseEntity.ok(comentarioRepository.findAll());
    }

    @Operation(summary = "Listar comentarios por publicación")
    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<Comentario>> getByPublicacion(@PathVariable Integer idPublicacion) {
        return ResponseEntity.ok(comentarioRepository.findByPublicacionIdPublicacion(idPublicacion));
    }

    @Operation(summary = "Obtener comentario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Comentario> getById(@PathVariable Integer id) {
        return comentarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear comentario")
    @PostMapping
    public ResponseEntity<Comentario> create(@RequestBody Comentario comentario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioRepository.save(comentario));
    }

    @Operation(summary = "Eliminar comentario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!comentarioRepository.existsById(id)) return ResponseEntity.notFound().build();
        comentarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
