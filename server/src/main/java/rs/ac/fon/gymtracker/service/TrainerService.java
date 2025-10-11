package rs.ac.fon.gymtracker.service;

import rs.ac.fon.gymtracker.domain.Trainer;

import java.util.Optional;

public interface TrainerService extends BaseCrudService<Trainer, Long> {
    Trainer login(String username, String rawPassword);
    Optional<Trainer> findByUsername(String username);
}
