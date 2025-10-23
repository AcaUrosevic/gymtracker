package rs.ac.fon.gymtracker.api.dto;

import java.util.List;

public record TrainerWithCertsDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String username,
        List<TrainerCertificateDto> certificates
) {}
