package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByClienteIdUsuario(Integer idCliente);
    List<Cita> findByArtistaIdUsuario(Integer idArtista);
    List<Cita> findByArtistaIdUsuarioAndEstado(Integer idArtista, Cita.EstadoCita estado);
}