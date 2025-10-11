package rs.ac.fon.gymtracker.service;

import rs.ac.fon.gymtracker.domain.Exercise;
import java.util.Optional;

public interface ExerciseService extends BaseCrudService<Exercise, Long> {
    Optional<Exercise> findByName(String name);
}
