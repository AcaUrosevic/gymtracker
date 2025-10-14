package rs.ac.fon.gymtracker.api.dto;

import java.time.LocalDate;

public record TrainerCertificateDto(
        Long trainerId,
        Long certificateId,
        String trainerName,
        String certificateName,
        LocalDate issuedAt
) {}
