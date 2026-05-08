package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Publicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {
    Page<Publicacion> findAllByOrderByCreadoEnDesc(Pageable pageable);
    Page<Publicacion> findByUsuarioIdUsuarioOrderByCreadoEnDesc(Integer idUsuario, Pageable pageable);
    Page<Publicacion> findByEstiloIgnoreCaseOrderByCreadoEnDesc(String estilo, Pageable pageable);
    long countByUsuarioIdUsuario(Integer idUsuario);
}