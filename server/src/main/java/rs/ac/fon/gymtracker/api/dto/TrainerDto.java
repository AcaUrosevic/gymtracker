package rs.ac.fon.gymtracker.api.dto;

public record TrainerDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String username
) {}
