package rs.ac.fon.gymtracker.service;

import rs.ac.fon.gymtracker.domain.Certificate;
import java.util.Optional;

public interface CertificateService extends BaseCrudService<Certificate, Long> {
    Optional<Certificate> findByName(String name);
}
