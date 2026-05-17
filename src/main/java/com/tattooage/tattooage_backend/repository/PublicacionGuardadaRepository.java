package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.PublicacionGuardada;
import com.tattooage.tattooage_backend.entity.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionGuardadaRepository extends JpaRepository<PublicacionGuardada, Integer> {

    boolean existsByPublicacionIdPublicacionAndUsuarioIdUsuario(Integer idPublicacion, Integer idUsuario);

    @org.springframework.transaction.annotation.Transactional
    void deleteByPublicacionIdPublicacionAndUsuarioIdUsuario(Integer idPublicacion, Integer idUsuario);

    @org.springframework.transaction.annotation.Transactional
    void deleteByPublicacionIdPublicacion(Integer idPublicacion);

    @Query("SELECT pg.publicacion FROM PublicacionGuardada pg WHERE pg.usuario.idUsuario = :idUsuario ORDER BY pg.creadoEn DESC")
    List<Publicacion> findPublicacionesByUsuarioId(@Param("idUsuario") Integer idUsuario);
}
