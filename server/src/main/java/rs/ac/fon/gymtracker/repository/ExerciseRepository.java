package rs.ac.fon.gymtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.fon.gymtracker.domain.Exercise;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByName(String name);
}
