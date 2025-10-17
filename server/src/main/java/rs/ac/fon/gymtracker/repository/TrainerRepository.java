package rs.ac.fon.gymtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.fon.gymtracker.domain.Trainer;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUsername(String username);
}
