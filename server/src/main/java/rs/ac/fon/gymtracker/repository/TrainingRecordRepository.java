package rs.ac.fon.gymtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.fon.gymtracker.domain.TrainingRecord;

import java.util.List;

public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long> {
    List<TrainingRecord> findByMemberIdOrderByTrainingDateDesc(Long memberId);
    List<TrainingRecord> findByTrainerIdOrderByTrainingDateDesc(Long trainerId);

}
