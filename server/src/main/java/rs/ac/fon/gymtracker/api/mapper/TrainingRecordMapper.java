package rs.ac.fon.gymtracker.api.mapper;

import rs.ac.fon.gymtracker.api.dto.TrainingRecordDto;
import rs.ac.fon.gymtracker.api.dto.TrainingRecordItemDto;
import rs.ac.fon.gymtracker.domain.TrainingRecord;
import rs.ac.fon.gymtracker.domain.TrainingRecordItem;

import java.util.stream.Collectors;

public final class TrainingRecordMapper {
    private TrainingRecordMapper() {}

    public static TrainingRecordItemDto toItemDto(TrainingRecordItem i) {
        return new TrainingRecordItemDto(
                i.getId().getRecordId(),
                i.getId().getRb(),
                i.getExercise().getId(),
                i.getExercise().getName(),
                i.getSets(),
                i.getReps(),
                i.getWeight()
        );
    }

    public static TrainingRecordDto toDto(TrainingRecord r) {
        var items = r.getItems().stream()
                .map(TrainingRecordMapper::toItemDto)
                .collect(Collectors.toList());

        var trainerName = r.getTrainer().getFirstName() + " " + r.getTrainer().getLastName();
        var memberName  = r.getMember().getFirstName() + " " + r.getMember().getLastName();

        return new TrainingRecordDto(
                r.getId(),
                r.getTrainingDate(),
                r.getIntensity(),
                r.getTrainer().getId(),
                trainerName,
                r.getMember().getId(),
                memberName,
                items
        );
    }
}
