package rs.ac.fon.gymtracker.api.dto;

public record MemberDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        Long packageId,
        String packageName
) {}
