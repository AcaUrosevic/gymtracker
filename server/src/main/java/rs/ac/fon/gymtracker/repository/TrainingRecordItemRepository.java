package rs.ac.fon.gymtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.fon.gymtracker.domain.TrainingRecordItem;
import rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId;

import java.util.List;

public interface TrainingRecordItemRepository extends JpaRepository<TrainingRecordItem, TrainingRecordItemId> {
    List<TrainingRecordItem> findByRecord_Id(Long recordId);
    void deleteByRecord_Id(Long recordId);
}
