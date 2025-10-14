package rs.ac.fon.gymtracker.api.mapper;

import rs.ac.fon.gymtracker.api.dto.CertificateDto;
import rs.ac.fon.gymtracker.domain.Certificate;

public final class CertificateMapper {
    private CertificateMapper() {}

    public static CertificateDto toDto(Certificate c) {
        return new CertificateDto(c.getId(), c.getName(), c.getType());
    }

    public static Certificate toEntity(CertificateDto dto) {
        var e = new Certificate();
        e.setId(dto.id());
        e.setName(dto.name());
        e.setType(dto.type());
        return e;
    }
}
