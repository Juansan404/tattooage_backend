package com.tattooage.tattooage_backend.repository;

import com.tattooage.tattooage_backend.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findTopByEmailAndUsadoFalseOrderByExpiraEnDesc(String email);

    void deleteByEmail(String email);
}
