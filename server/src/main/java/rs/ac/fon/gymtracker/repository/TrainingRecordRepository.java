package rs.ac.fon.gymtracker.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import rs.ac.fon.gymtracker.domain.TrainingRecord;

import java.util.List;
import java.util.Optional;

public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long> {

    @EntityGraph(attributePaths = {
            "trainer",
            "member",
            "items",
            "items.exercise"
    })
    @NonNull
    Optional<TrainingRecord> findById(@NonNull Long id);


    @EntityGraph(attributePaths = {"trainer", "member", "items", "items.exercise"})
    List<TrainingRecord> findByMemberIdOrderByTrainingDateDesc(Long memberId);

    @EntityGraph(attributePaths = {"trainer", "member", "items", "items.exercise"})
    List<TrainingRecord> findByTrainerIdOrderByTrainingDateDesc(Long trainerId);
}
