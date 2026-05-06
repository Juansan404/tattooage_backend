package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversacionRepository extends JpaRepository<Conversacion, Integer> {

    @Query("SELECT c FROM Conversacion c WHERE c.usuario1.idUsuario = :id OR c.usuario2.idUsuario = :id ORDER BY c.creadoEn DESC")
    List<Conversacion> findByUsuarioId(@Param("id") Integer idUsuario);

    @Query("SELECT c FROM Conversacion c WHERE (c.usuario1.idUsuario = :u1 AND c.usuario2.idUsuario = :u2) OR (c.usuario1.idUsuario = :u2 AND c.usuario2.idUsuario = :u1)")
    Optional<Conversacion> findByUsuarios(@Param("u1") Integer idU1, @Param("u2") Integer idU2);
}
