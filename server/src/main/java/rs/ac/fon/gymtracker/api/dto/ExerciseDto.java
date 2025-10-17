package rs.ac.fon.gymtracker.api.dto;

public record ExerciseDto(
        Long id,
        String name,
        String description,
        double effort
) {}
