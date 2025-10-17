package rs.ac.fon.gymtracker.api.mapper;

import rs.ac.fon.gymtracker.api.dto.ServicePackageDto;
import rs.ac.fon.gymtracker.domain.ServicePackage;

public final class ServicePackageMapper {
    private ServicePackageMapper() {}

    public static ServicePackageDto toDto(ServicePackage sp) {
        return new ServicePackageDto(
                sp.getId(), sp.getName(), sp.getDescription(),
                sp.getDurationDays(), sp.getPrice()
        );
    }

    public static ServicePackage toEntity(ServicePackageDto dto) {
        var e = new ServicePackage();
        e.setId(dto.id());
        e.setName(dto.name());
        e.setDescription(dto.description());
        e.setDurationDays(dto.durationDays());
        e.setPrice(dto.price());
        return e;
    }
}
