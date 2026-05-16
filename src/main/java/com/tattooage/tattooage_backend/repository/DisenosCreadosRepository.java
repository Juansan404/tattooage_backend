package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.DisenoCreado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DisenosCreadosRepository extends JpaRepository<DisenoCreado, Integer> {
    List<DisenoCreado> findByUsuarioIdUsuarioOrderByCreadoEnDesc(Integer idUsuario);
}
