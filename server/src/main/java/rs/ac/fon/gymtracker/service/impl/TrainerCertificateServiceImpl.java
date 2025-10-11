package rs.ac.fon.gymtracker.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.fon.gymtracker.domain.Certificate;
import rs.ac.fon.gymtracker.domain.Trainer;
import rs.ac.fon.gymtracker.domain.TrainerCertificate;
import rs.ac.fon.gymtracker.domain.id.TrainerCertificateId;
import rs.ac.fon.gymtracker.repository.CertificateRepository;
import rs.ac.fon.gymtracker.repository.TrainerCertificateRepository;
import rs.ac.fon.gymtracker.repository.TrainerRepository;
import rs.ac.fon.gymtracker.service.TrainerCertificateService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TrainerCertificateServiceImpl implements TrainerCertificateService {

    private final TrainerCertificateRepository repo;
    private final TrainerRepository trainerRepo;
    private final CertificateRepository certificateRepo;

    public TrainerCertificateServiceImpl(TrainerCertificateRepository repo,
                                         TrainerRepository trainerRepo,
                                         CertificateRepository certificateRepo) {
        this.repo = repo;
        this.trainerRepo = trainerRepo;
        this.certificateRepo = certificateRepo;
    }

    @Override
    public TrainerCertificate assign(Long trainerId, Long certificateId, LocalDate issuedAt) {
        Trainer t = trainerRepo.findById(trainerId).orElseThrow();
        Certificate c = certificateRepo.findById(certificateId).orElseThrow();

        var id = new TrainerCertificateId(trainerId, certificateId);
        if (repo.existsById(id)) {
            var existing = repo.findById(id).orElseThrow();
            existing.setIssuedAt(issuedAt);
            return repo.save(existing);
        }

        var tc = new TrainerCertificate();
        tc.setId(id);
        tc.setTrainer(t);
        tc.setCertificate(c);
        tc.setIssuedAt(issuedAt);
        return repo.save(tc);
    }

    @Override
    public void revoke(Long trainerId, Long certificateId) {
        repo.deleteById(new TrainerCertificateId(trainerId, certificateId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerCertificate> listForTrainer(Long trainerId) {
        return repo.findByTrainerId(trainerId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(TrainerCertificateId id) {
        return repo.existsById(id);
    }
}
