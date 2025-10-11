package rs.ac.fon.gymtracker.service;

import rs.ac.fon.gymtracker.domain.TrainerCertificate;
import rs.ac.fon.gymtracker.domain.id.TrainerCertificateId;

import java.time.LocalDate;
import java.util.List;

public interface TrainerCertificateService {
    TrainerCertificate assign(Long trainerId, Long certificateId, LocalDate issuedAt);
    void revoke(Long trainerId, Long certificateId);
    List<TrainerCertificate> listForTrainer(Long trainerId);
    boolean exists(TrainerCertificateId id);
}
