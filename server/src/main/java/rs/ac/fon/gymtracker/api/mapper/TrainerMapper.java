package rs.ac.fon.gymtracker.api.mapper;

import rs.ac.fon.gymtracker.api.dto.TrainerDto;
import rs.ac.fon.gymtracker.api.dto.TrainerWithCertsDto;
import rs.ac.fon.gymtracker.domain.Trainer;
import rs.ac.fon.gymtracker.domain.TrainerCertificate;

import java.util.List;

public final class TrainerMapper {
    private TrainerMapper() {}

    public static TrainerDto toDto(Trainer t) {
        return new TrainerDto(t.getId(), t.getFirstName(), t.getLastName(), t.getEmail(), t.getUsername());
    }

    public static Trainer toEntity(TrainerDto dto) {
        var e = new Trainer();
        e.setId(dto.id());
        e.setFirstName(dto.firstName());
        e.setLastName(dto.lastName());
        e.setEmail(dto.email());
        e.setUsername(dto.username());
        return e;
    }

    public static TrainerWithCertsDto toWithCertsDto(Trainer t, List<TrainerCertificate> certs) {
        var certDtos = certs.stream()
                .map(TrainerCertificateMapper::toDto)
                .toList();
        return new TrainerWithCertsDto(
                t.getId(), t.getFirstName(), t.getLastName(), t.getEmail(), t.getUsername(), certDtos
        );
    }
}
