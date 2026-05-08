package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Seguidor;
import com.tattooage.tattooage_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguidorRepository extends JpaRepository<Seguidor, Integer> {

    boolean existsBySeguidorIdUsuarioAndSeguidoIdUsuario(Integer idSeguidor, Integer idSeguido);

    void deleteBySeguidorIdUsuarioAndSeguidoIdUsuario(Integer idSeguidor, Integer idSeguido);

    long countBySeguidoIdUsuario(Integer idSeguido);

    long countBySeguidorIdUsuario(Integer idSeguidor);

    @Query("SELECT s.seguidor FROM Seguidor s WHERE s.seguido.idUsuario = :id")
    List<Usuario> findSeguidoresBySeguidoId(@Param("id") Integer idSeguido);

    @Query("SELECT s.seguido FROM Seguidor s WHERE s.seguidor.idUsuario = :id")
    List<Usuario> findSeguidosBySeguidorId(@Param("id") Integer idSeguidor);

    @Query("SELECT s.seguido.idUsuario, COUNT(s) FROM Seguidor s GROUP BY s.seguido.idUsuario")
    List<Object[]> countSeguidoresGrouped();

    @Query("SELECT s.seguidor.idUsuario, COUNT(s) FROM Seguidor s GROUP BY s.seguidor.idUsuario")
    List<Object[]> countSeguidosGrouped();
}
