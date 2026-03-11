package com.tattooage.tattooage_backend.service;

import com.tattooage.tattooage_backend.entity.Publicacion;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.PublicacionRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;

    public Page<Publicacion> getFeed(Pageable pageable) {
        return publicacionRepository.findAllByOrderByCreadoEnDesc(pageable);
    }

    public Page<Publicacion> getByUsuario(Integer idUsuario, Pageable pageable) {
        return publicacionRepository.findByUsuarioIdUsuarioOrderByCreadoEnDesc(idUsuario, pageable);
    }

    public Page<Publicacion> getByEstilo(String estilo, Pageable pageable) {
        return publicacionRepository.findByEstiloIgnoreCaseOrderByCreadoEnDesc(estilo, pageable);
    }

    public Publicacion getById(Integer id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publicación no encontrada"));
    }

    public Publicacion crear(Publicacion publicacion, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        publicacion.setUsuario(usuario);
        return publicacionRepository.save(publicacion);
    }

    public void eliminar(Integer id, String email) {
        Publicacion publicacion = getById(id);
        if (!publicacion.getUsuario().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar esta publicación");
        }
        publicacionRepository.deleteById(id);
    }
}