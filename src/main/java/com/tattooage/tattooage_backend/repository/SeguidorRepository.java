package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Seguidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeguidorRepository extends JpaRepository<Seguidor, Integer> {

    boolean existsBySeguidorIdUsuarioAndSeguidoIdUsuario(Integer idSeguidor, Integer idSeguido);

    void deleteBySeguidorIdUsuarioAndSeguidoIdUsuario(Integer idSeguidor, Integer idSeguido);

    long countBySeguidoIdUsuario(Integer idSeguido);

    long countBySeguidorIdUsuario(Integer idSeguidor);
}
