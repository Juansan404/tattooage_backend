package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Estilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstiloRepository extends JpaRepository<Estilo, Integer> {

    Optional<Estilo> findByNombreIgnoreCase(String nombre);

    @Query(value =
        "SELECT e.* FROM estilos e " +
        "LEFT JOIN publicacion_estilos pe ON e.id_estilo = pe.id_estilo " +
        "LEFT JOIN publicaciones p ON pe.id_publicacion = p.id_publicacion " +
        "GROUP BY e.id_estilo " +
        "ORDER BY COALESCE(SUM(p.likes_count), 0) DESC",
        nativeQuery = true)
    List<Estilo> findAllOrderByLikesDesc();
}
