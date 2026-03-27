package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Publicacion;
import com.tattooage.tattooage_backend.repository.PublicacionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Publicaciones", description = "Gestión de publicaciones de tatuajes")
@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionRepository publicacionRepository;

    @Operation(summary = "Listar publicaciones", description = "Devuelve todas las publicaciones. Acceso público.")
    @GetMapping
    public ResponseEntity<List<Publicacion>> getAll() {
        return ResponseEntity.ok(publicacionRepository.findAll());
    }

    @Operation(summary = "Obtener publicación por ID", description = "Devuelve una publicación concreta. Devuelve 404 si no existe.")
    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> getById(@PathVariable Integer id) {
        return publicacionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear publicación")
    @PostMapping
    public ResponseEntity<Publicacion> create(@RequestBody Publicacion publicacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publicacionRepository.save(publicacion));
    }

    @Operation(summary = "Actualizar publicación")
    @PutMapping("/{id}")
    public ResponseEntity<Publicacion> update(@PathVariable Integer id, @RequestBody Map<String, Object> datos) {
        return publicacionRepository.findById(id).map(p -> {
            if (datos.get("fotoUrl")     != null) p.setFotoUrl(datos.get("fotoUrl").toString());
            if (datos.get("descripcion") != null) p.setDescripcion(datos.get("descripcion").toString());
            if (datos.get("estilo")      != null) p.setEstilo(datos.get("estilo").toString());
            if (datos.get("zonaCuerpo")  != null) p.setZonaCuerpo(datos.get("zonaCuerpo").toString());
            return ResponseEntity.ok(publicacionRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar publicación", description = "Elimina una publicación por ID. Requiere autenticación.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!publicacionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        publicacionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}