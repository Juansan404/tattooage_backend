package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Estudio;
import com.tattooage.tattooage_backend.entity.PerfilArtista;
import com.tattooage.tattooage_backend.repository.EstudioRepository;
import com.tattooage.tattooage_backend.repository.PerfilArtistaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Estudios", description = "Gestión de estudios de tatuaje. Requiere autenticación.")
@RestController
@RequestMapping("/api/estudios")
@RequiredArgsConstructor
public class EstudioController {

    private final EstudioRepository estudioRepository;
    private final PerfilArtistaRepository perfilArtistaRepository;

    @Operation(summary = "Listar estudios")
    @GetMapping
    public ResponseEntity<List<Estudio>> getAll() {
        return ResponseEntity.ok(estudioRepository.findAll());
    }

    @Operation(summary = "Obtener estudio por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Estudio> getById(@PathVariable Integer id) {
        return estudioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear estudio")
    @PostMapping
    public ResponseEntity<Estudio> create(@RequestBody Estudio estudio) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(estudioRepository.save(estudio));
    }

    @Operation(summary = "Actualizar estudio")
    @PutMapping("/{id}")
    public ResponseEntity<Estudio> update(@PathVariable Integer id, @RequestBody Estudio datos) {
        return estudioRepository.findById(id).map(e -> {
            if (datos.getNombre()       != null) e.setNombre(datos.getNombre());
            if (datos.getDireccion()    != null) e.setDireccion(datos.getDireccion());
            if (datos.getCiudad()       != null) e.setCiudad(datos.getCiudad());
            if (datos.getTelefono()     != null) e.setTelefono(datos.getTelefono());
            if (datos.getEmail()        != null) e.setEmail(datos.getEmail());
            if (datos.getDescripcion()  != null) e.setDescripcion(datos.getDescripcion());
            if (datos.getWeb()          != null) e.setWeb(datos.getWeb());
            if (datos.getInstagram()    != null) e.setInstagram(datos.getInstagram());
            if (datos.getLocalizacion() != null) e.setLocalizacion(datos.getLocalizacion());
            return ResponseEntity.ok(estudioRepository.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Artistas de un estudio")
    @GetMapping("/{id}/artistas")
    public ResponseEntity<List<PerfilArtista>> getArtistas(@PathVariable Integer id) {
        return ResponseEntity.ok(perfilArtistaRepository.findByEstudioIdEstudio(id));
    }

    @Operation(summary = "Eliminar estudio")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!estudioRepository.existsById(id)) return ResponseEntity.notFound().build();
        estudioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
