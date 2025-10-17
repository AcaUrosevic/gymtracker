package rs.ac.fon.gymtracker.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import rs.ac.fon.gymtracker.domain.TrainingRecord;

import java.util.List;
import java.util.Optional;

public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long>, JpaSpecificationExecutor<TrainingRecord> {
    @EntityGraph(attributePaths = {"trainer", "member", "items", "items.exercise"})
    @NonNull
    List<TrainingRecord> findAll();

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

    @EntityGraph(attributePaths = {"trainer", "member", "items", "items.exercise"})
    @NonNull
    List<TrainingRecord> findAll(Specification<TrainingRecord> spec);
}
