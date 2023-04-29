package com.company.certificate.repository;

import com.company.certificate.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
    Optional<Certificate> findByTokenAndDeletedAtIsNull(String token);
}
