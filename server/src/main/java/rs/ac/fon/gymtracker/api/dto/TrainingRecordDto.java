package rs.ac.fon.gymtracker.api.dto;

import java.time.LocalDate;
import java.util.List;

public record TrainingRecordDto(
        Long id,
        LocalDate trainingDate,
        double intensity,
        Long trainerId,
        String trainerName,
        Long memberId,
        String memberName,
        List<TrainingRecordItemDto> items
) {}
