package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Like;
import com.tattooage.tattooage_backend.entity.Notificacion;
import com.tattooage.tattooage_backend.entity.Publicacion;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.LikeRepository;
import com.tattooage.tattooage_backend.repository.NotificacionRepository;
import com.tattooage.tattooage_backend.repository.PublicacionRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Publicaciones", description = "Gestión de publicaciones de tatuajes")
@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionRepository publicacionRepository;
    private final LikeRepository likeRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionRepository notificacionRepository;

    @Operation(summary = "Listar publicaciones",
               description = "Sin parámetros devuelve todas. Con ?page=0&size=15 devuelve Page<Publicacion> paginado, ordenado por fecha desc.")
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(defaultValue = "15")  int size) {

        if (page == null) {
            List<Publicacion> todas = publicacionRepository.findAll(
                    Sort.by("creadoEn").descending());
            return ResponseEntity.ok(todas);
        }
        Page<Publicacion> resultado = publicacionRepository
                .findAllByOrderByCreadoEnDesc(PageRequest.of(page, size));
        return ResponseEntity.ok(resultado);
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

    @Operation(summary = "Toggle like", description = "Da o quita like a una publicación. Devuelve el estado actualizado.")
    @PostMapping("/{id}/like")
    @Transactional
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body) {

        Integer idUsuario = body.get("idUsuario");
        if (idUsuario == null) return ResponseEntity.badRequest().build();

        Publicacion publicacion = publicacionRepository.findById(id).orElse(null);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (publicacion == null || usuario == null) return ResponseEntity.notFound().build();

        boolean yaLiked = likeRepository.existsByPublicacionIdPublicacionAndUsuarioIdUsuario(id, idUsuario);

        int count = publicacion.getLikesCount() != null ? publicacion.getLikesCount() : 0;
        if (yaLiked) {
            likeRepository.deleteByPublicacionIdPublicacionAndUsuarioIdUsuario(id, idUsuario);
            publicacion.setLikesCount(Math.max(0, count - 1));
        } else {
            likeRepository.save(Like.builder().publicacion(publicacion).usuario(usuario).build());
            publicacion.setLikesCount(count + 1);
            if (publicacion.getUsuario() != null && !publicacion.getUsuario().getIdUsuario().equals(idUsuario)) {
                notificacionRepository.save(Notificacion.builder()
                        .receptor(publicacion.getUsuario())
                        .emisor(usuario)
                        .tipo(Notificacion.TipoNotificacion.LIKE)
                        .idReferencia(id)
                        .build());
            }
        }
        publicacionRepository.save(publicacion);

        long count = likeRepository.countByPublicacionIdPublicacion(id);
        return ResponseEntity.ok(Map.of("liked", !yaLiked, "likesCount", count));
    }

    @Operation(summary = "Comprobar si el usuario ha dado like")
    @GetMapping("/{id}/like/{idUsuario}")
    public ResponseEntity<Map<String, Object>> getLikeStatus(
            @PathVariable Integer id,
            @PathVariable Integer idUsuario) {

        boolean liked = likeRepository.existsByPublicacionIdPublicacionAndUsuarioIdUsuario(id, idUsuario);
        long count = likeRepository.countByPublicacionIdPublicacion(id);
        return ResponseEntity.ok(Map.of("liked", liked, "likesCount", count));
    }
}