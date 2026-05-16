package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.PerfilArtista;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.PerfilArtistaRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Tag(name = "Artistas", description = "Consulta de artistas y sus perfiles. Acceso público.")
@RestController
@RequestMapping("/api/artistas")
@RequiredArgsConstructor
public class ArtistaController {

    private final UsuarioRepository       usuarioRepository;
    private final PerfilArtistaRepository perfilArtistaRepository;

    @Operation(summary = "Listar artistas", description = "Devuelve todos los usuarios con rol ARTISTA y estado ACTIVO, con su perfil embebido si existe.")
    @GetMapping
    public ResponseEntity<List<PerfilArtista>> getAll() {
        List<PerfilArtista> result = usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == Usuario.RolUsuario.ARTISTA
                          && u.getEstadoRegistro() == Usuario.EstadoRegistro.ACTIVO)
                .map(u -> perfilArtistaRepository.findByUsuarioIdUsuario(u.getIdUsuario())
                        .orElseGet(() -> {
                            PerfilArtista p = new PerfilArtista();
                            p.setUsuario(u);
                            p.setDisponible(false);
                            return p;
                        }))
                .toList();
        return ResponseEntity.ok(result);
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

    @Operation(summary = "Crear perfil para un artista existente")
    @PostMapping("/{id}/perfil")
    public ResponseEntity<?> createPerfil(@PathVariable Integer id,
                                          @RequestBody PerfilArtista datos) {
        return usuarioRepository.findById(id).map(u -> {
            if (perfilArtistaRepository.findByUsuarioIdUsuario(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body((Object) "El artista ya tiene un perfil");
            }
            datos.setUsuario(u);
            PerfilArtista saved = perfilArtistaRepository.save(datos);
            return ResponseEntity.status(HttpStatus.CREATED).body((Object) saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar perfil del artista")
    @PutMapping("/{id}/perfil")
    public ResponseEntity<PerfilArtista> updatePerfil(@PathVariable Integer id,
                                                       @RequestBody Map<String, Object> datos) {
        return perfilArtistaRepository.findByUsuarioIdUsuario(id).map(p -> {
            if (datos.get("especialidades") != null) p.setEspecialidades(datos.get("especialidades").toString());
            if (datos.get("anosExperiencia") != null) p.setAnosExperiencia(Integer.valueOf(datos.get("anosExperiencia").toString()));
            if (datos.get("instagram")       != null) p.setInstagram(datos.get("instagram").toString());
            if (datos.get("precioHora")      != null) p.setPrecioHora(new BigDecimal(datos.get("precioHora").toString()));
            if (datos.get("disponible")      != null) p.setDisponible(Boolean.valueOf(datos.get("disponible").toString()));
            if (datos.get("portfolioUrl")    != null) p.setPortfolioUrl(datos.get("portfolioUrl").toString());
            return ResponseEntity.ok(perfilArtistaRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }
}