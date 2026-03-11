package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.SolicitudCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudCitaRepository extends JpaRepository<SolicitudCita, Integer> {
    List<SolicitudCita> findByClienteIdUsuario(Integer idCliente);
    List<SolicitudCita> findByArtistaIdUsuario(Integer idArtista);
    List<SolicitudCita> findByArtistaIdUsuarioAndEstado(Integer idArtista, SolicitudCita.EstadoSolicitud estado);
}