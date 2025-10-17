package rs.ac.fon.gymtracker.api.mapper;

import rs.ac.fon.gymtracker.api.dto.ExerciseDto;
import rs.ac.fon.gymtracker.domain.Exercise;

public final class ExerciseMapper {
    private ExerciseMapper() {}

    public static ExerciseDto toDto(Exercise ex) {
        return new ExerciseDto(ex.getId(), ex.getName(), ex.getDescription(), ex.getEffort());
    }
    public static Exercise toEntity(ExerciseDto dto) {
        var e = new Exercise();
        e.setId(dto.id());
        e.setName(dto.name());
        e.setDescription(dto.description());
        e.setEffort(dto.effort());
        return e;
    }
}
