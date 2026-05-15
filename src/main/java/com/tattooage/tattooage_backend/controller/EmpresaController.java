package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Empresa;
import com.tattooage.tattooage_backend.entity.PerfilArtista;
import com.tattooage.tattooage_backend.repository.EmpresaRepository;
import com.tattooage.tattooage_backend.repository.PerfilArtistaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Empresas", description = "Gestión de empresas de tatuaje")
@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaRepository    empresaRepository;
    private final PerfilArtistaRepository perfilArtistaRepository;

    @Operation(summary = "Listar todas las empresas")
    @GetMapping
    public ResponseEntity<List<Empresa>> getAll() {
        return ResponseEntity.ok(empresaRepository.findAll());
    }

    @Operation(summary = "Obtener empresa por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> getById(@PathVariable Integer id) {
        return empresaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Artistas de una empresa")
    @GetMapping("/{id}/artistas")
    public ResponseEntity<List<PerfilArtista>> getArtistas(@PathVariable Integer id) {
        return ResponseEntity.ok(
                perfilArtistaRepository.findByEmpresaIdEmpresa(id)
        );
    }

    @Operation(summary = "Crear empresa")
    @PostMapping
    public ResponseEntity<Empresa> create(@RequestBody Empresa empresa) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(empresaRepository.save(empresa));
    }

    @Operation(summary = "Actualizar empresa")
    @PutMapping("/{id}")
    public ResponseEntity<Empresa> update(@PathVariable Integer id, @RequestBody Empresa datos) {
        return empresaRepository.findById(id).map(e -> {
            if (datos.getNombre()       != null) e.setNombre(datos.getNombre());
            if (datos.getDescripcion()  != null) e.setDescripcion(datos.getDescripcion());
            if (datos.getCiudad()       != null) e.setCiudad(datos.getCiudad());
            if (datos.getLocalizacion() != null) e.setLocalizacion(datos.getLocalizacion());
            if (datos.getEmail()        != null) e.setEmail(datos.getEmail());
            if (datos.getTelefono()     != null) e.setTelefono(datos.getTelefono());
            if (datos.getWeb()          != null) e.setWeb(datos.getWeb());
            if (datos.getLogo()         != null) e.setLogo(datos.getLogo());
            return ResponseEntity.ok(empresaRepository.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar empresa")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!empresaRepository.existsById(id)) return ResponseEntity.notFound().build();
        // Desvincula artistas antes de eliminar
        perfilArtistaRepository.findByEmpresaIdEmpresa(id)
                .forEach(p -> { p.setEmpresa(null); perfilArtistaRepository.save(p); });
        empresaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
