package rs.ac.fon.gymtracker.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.fon.gymtracker.domain.Certificate;
import rs.ac.fon.gymtracker.repository.CertificateRepository;
import rs.ac.fon.gymtracker.service.CertificateService;

import java.util.Optional;

@Service
@Transactional
public class CertificateServiceImpl
        extends AbstractJpaCrudService<Certificate, Long, CertificateRepository>
        implements CertificateService {

    public CertificateServiceImpl(CertificateRepository repo) { super(repo); }

    @Override
    protected Certificate doMergeAndSave(Certificate current, Certificate patch) {
        if (patch.getName() != null) current.setName(patch.getName());
        if (patch.getType() != null) current.setType(patch.getType());
        return repo.save(current);
    }
}
