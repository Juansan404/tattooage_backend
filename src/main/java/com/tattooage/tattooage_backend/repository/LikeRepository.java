package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    boolean existsByPublicacionIdPublicacionAndUsuarioIdUsuario(Integer idPublicacion, Integer idUsuario);

    void deleteByPublicacionIdPublicacionAndUsuarioIdUsuario(Integer idPublicacion, Integer idUsuario);

    long countByPublicacionIdPublicacion(Integer idPublicacion);
}
