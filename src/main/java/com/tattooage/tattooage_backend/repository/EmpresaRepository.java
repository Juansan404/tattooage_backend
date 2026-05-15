package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
}
