package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    List<Mensaje> findBySolicitudIdSolicitud(Integer idSolicitud);
}
