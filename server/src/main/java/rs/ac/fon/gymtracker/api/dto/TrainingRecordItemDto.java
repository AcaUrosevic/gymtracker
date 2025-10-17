package rs.ac.fon.gymtracker.api.dto;

public record TrainingRecordItemDto(
        Long recordId,
        Integer rb,
        Long exerciseId,
        String exerciseName,
        int sets,
        int reps,
        double weight
) {}
