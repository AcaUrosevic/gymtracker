package rs.ac.fon.gymtracker.api.mapper;

import rs.ac.fon.gymtracker.api.dto.TrainerCertificateDto;
import rs.ac.fon.gymtracker.domain.TrainerCertificate;

public final class TrainerCertificateMapper {
    private TrainerCertificateMapper() {}

    public static TrainerCertificateDto toDto(TrainerCertificate tc) {
        return new TrainerCertificateDto(
                tc.getTrainer().getId(),
                tc.getCertificate().getId(),
                tc.getTrainer().getFirstName() + " " + tc.getTrainer().getLastName(),
                tc.getCertificate().getName(),
                tc.getIssuedAt()
        );
    }
}
