package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.PerfilArtista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilArtistaRepository extends JpaRepository<PerfilArtista, Integer> {
    Optional<PerfilArtista> findByUsuarioIdUsuario(Integer idUsuario);
    List<PerfilArtista> findByDisponibleTrue();
    List<PerfilArtista> findByEmpresaIdEmpresa(Integer idEmpresa);
}