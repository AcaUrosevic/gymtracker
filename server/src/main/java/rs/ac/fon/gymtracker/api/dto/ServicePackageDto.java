package rs.ac.fon.gymtracker.api.dto;

public record ServicePackageDto(
        Long id,
        String name,
        String description,
        int durationDays,
        int price
) {}
