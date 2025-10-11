package rs.ac.fon.gymtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.fon.gymtracker.domain.Certificate;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Optional<Certificate> findByName(String name);
}
