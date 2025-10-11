package rs.ac.fon.gymtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.fon.gymtracker.domain.*;

import java.util.Optional;

public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {
    Optional<ServicePackage> findByName(String name);
}
