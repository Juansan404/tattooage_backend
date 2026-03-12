package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Cita;
import com.tattooage.tattooage_backend.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaRepository citaRepository;

    @GetMapping
    public ResponseEntity<List<Cita>> getAll() {
        return ResponseEntity.ok(citaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> getById(@PathVariable Integer id) {
        return citaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cita> create(@RequestBody Cita cita) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(citaRepository.save(cita));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!citaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        citaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}