package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByReceptorIdUsuarioOrderByCreadoEnDesc(Integer idUsuario);

    long countByReceptorIdUsuarioAndLeidaFalse(Integer idUsuario);

    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true WHERE n.receptor.idUsuario = :idUsuario")
    void marcarTodasLeidas(@Param("idUsuario") Integer idUsuario);

    @Modifying
    @Query("DELETE FROM Notificacion n WHERE n.idReferencia = :idReferencia")
    void deleteByIdReferencia(@Param("idReferencia") Integer idReferencia);
}
