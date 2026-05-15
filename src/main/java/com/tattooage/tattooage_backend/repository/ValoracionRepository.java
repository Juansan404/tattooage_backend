package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ValoracionRepository extends JpaRepository<Valoracion, Integer> {

    List<Valoracion> findByArtistaIdUsuarioOrderByCreadoEnDesc(Integer idArtista);

    Optional<Valoracion> findByArtistaIdUsuarioAndClienteIdUsuario(Integer idArtista, Integer idCliente);

    @Query("SELECT AVG(v.puntuacion) FROM Valoracion v WHERE v.artista.idUsuario = :idArtista")
    Double findAvgPuntuacion(@Param("idArtista") Integer idArtista);

    Long countByArtistaIdUsuario(Integer idArtista);
}
