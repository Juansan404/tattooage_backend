package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
    List<Comentario> findByPublicacionIdPublicacion(Integer idPublicacion);
}
