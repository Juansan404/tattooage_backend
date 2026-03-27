package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.Estudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudioRepository extends JpaRepository<Estudio, Integer> {
}
