package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Publicacion;
import com.tattooage.tattooage_backend.repository.PublicacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionRepository publicacionRepository;

    @GetMapping
    public ResponseEntity<List<Publicacion>> getAll() {
        return ResponseEntity.ok(publicacionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> getById(@PathVariable Integer id) {
        return publicacionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!publicacionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        publicacionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}