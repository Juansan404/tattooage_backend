package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    boolean existsByPublicacionIdPublicacionAndUsuarioIdUsuario(Integer idPublicacion, Integer idUsuario);

    @Transactional
    void deleteByPublicacionIdPublicacionAndUsuarioIdUsuario(Integer idPublicacion, Integer idUsuario);

    @Transactional
    void deleteByPublicacionIdPublicacion(Integer idPublicacion);

    long countByPublicacionIdPublicacion(Integer idPublicacion);
}
