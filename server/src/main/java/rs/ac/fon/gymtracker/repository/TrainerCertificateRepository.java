package rs.ac.fon.gymtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.fon.gymtracker.domain.TrainerCertificate;
import rs.ac.fon.gymtracker.domain.id.TrainerCertificateId;

import java.util.List;

public interface TrainerCertificateRepository extends JpaRepository<TrainerCertificate, TrainerCertificateId> {
    List<TrainerCertificate> findByTrainerId(Long trainerId);

}
