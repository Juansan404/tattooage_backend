package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByRolAndActivoTrue(Usuario.RolUsuario rol);
    List<Usuario> findByEstadoRegistro(Usuario.EstadoRegistro estado);
    List<Usuario> findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombre, String apellidos);
}