package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.DisenoCreado;
import com.tattooage.tattooage_backend.repository.DisenosCreadosRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Diseños Creados", description = "Diseños locales del usuario sincronizados en la nube")
@RestController
@RequestMapping("/api/disenos-creados")
@RequiredArgsConstructor
public class DisenosCreadosController {

    private final DisenosCreadosRepository disenosRepo;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<DisenoCreado>> getByUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(disenosRepo.findByUsuarioIdUsuarioOrderByCreadoEnDesc(idUsuario));
    }

    @PostMapping
    public ResponseEntity<DisenoCreado> create(@RequestBody Map<String, Object> body) {
        DisenoCreado diseno = new DisenoCreado();
        if (body.get("idUsuario") instanceof Integer id) {
            diseno.setUsuario(usuarioRepository.findById(id).orElseThrow());
        }
        if (body.get("nombre") != null)  diseno.setNombre(body.get("nombre").toString());
        if (body.get("fotoUrl") != null) diseno.setFotoUrl(body.get("fotoUrl").toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(disenosRepo.save(diseno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!disenosRepo.existsById(id)) return ResponseEntity.notFound().build();
        disenosRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
