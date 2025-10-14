package rs.ac.fon.gymtracker.api.mapper;

import rs.ac.fon.gymtracker.api.dto.MemberDto;
import rs.ac.fon.gymtracker.domain.Member;
import rs.ac.fon.gymtracker.domain.ServicePackage;

public final class MemberMapper {
    private MemberMapper() {}

    public static MemberDto toDto(Member m) {
        return new MemberDto(
                m.getId(), m.getFirstName(), m.getLastName(), m.getEmail(),
                m.getServicePackage() != null ? m.getServicePackage().getId() : null,
                m.getServicePackage() != null ? m.getServicePackage().getName() : null
        );
    }

    public static Member toEntity(MemberDto dto, ServicePackage pkg) {
        var e = new Member();
        e.setId(dto.id());
        e.setFirstName(dto.firstName());
        e.setLastName(dto.lastName());
        e.setEmail(dto.email());
        e.setServicePackage(pkg);
        return e;
    }
}
