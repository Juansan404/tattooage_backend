package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.MensajeDirecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MensajeDirectoRepository extends JpaRepository<MensajeDirecto, Integer> {

    List<MensajeDirecto> findByConversacionIdConversacionOrderByCreadoEnAsc(Integer idConversacion);

    long countByConversacionIdConversacionAndRemitenteIdUsuarioNotAndLeidoFalse(Integer idConversacion, Integer idRemitente);

    @Modifying
    @Transactional
    @Query("UPDATE MensajeDirecto m SET m.leido = true WHERE m.conversacion.idConversacion = :idConv AND m.remitente.idUsuario <> :idUsuario")
    void marcarLeidosEnConversacion(@Param("idConv") Integer idConversacion, @Param("idUsuario") Integer idUsuario);

    @Modifying
    @Transactional
    @Query("DELETE FROM MensajeDirecto m WHERE m.conversacion.idConversacion = :idConv")
    void deleteByConversacionIdConversacion(@Param("idConv") Integer idConversacion);
}
