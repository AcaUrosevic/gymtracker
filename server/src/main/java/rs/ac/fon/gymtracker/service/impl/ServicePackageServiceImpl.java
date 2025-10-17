package rs.ac.fon.gymtracker.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.fon.gymtracker.domain.ServicePackage;
import rs.ac.fon.gymtracker.repository.ServicePackageRepository;
import rs.ac.fon.gymtracker.service.ServicePackageService;

import java.util.Optional;

@Service
@Transactional
public class ServicePackageServiceImpl
        extends AbstractJpaCrudService<ServicePackage, Long, ServicePackageRepository>
        implements ServicePackageService {

    public ServicePackageServiceImpl(ServicePackageRepository repo) { super(repo); }

    @Override
    protected ServicePackage doMergeAndSave(ServicePackage current, ServicePackage patch) {
        if (patch.getName() != null)         current.setName(patch.getName());
        if (patch.getDescription() != null)  current.setDescription(patch.getDescription());
        if (patch.getDurationDays() > 0)     current.setDurationDays(patch.getDurationDays());
        if (patch.getPrice() > 0)           current.setPrice(patch.getPrice());
        return repo.save(current);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServicePackage> findByName(String name) {
        return repo.findByName(name);
    }
}
