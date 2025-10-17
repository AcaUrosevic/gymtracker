package rs.ac.fon.gymtracker.service;

import rs.ac.fon.gymtracker.domain.TrainingRecord;
import rs.ac.fon.gymtracker.domain.TrainingRecordItem;
import rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRecordService extends BaseCrudService<TrainingRecord, Long> {

    TrainingRecord createRecord(LocalDate date, Long trainerId, Long memberId);

    TrainingRecordItem addItem(Long recordId, Long exerciseId, int sets, int reps, double weight);

    TrainingRecord recalcIntensity(Long recordId);

    List<TrainingRecord> listByMember(Long memberId);

    List<TrainingRecord> listByTrainer(Long trainerId);

    void deleteRecord(Long recordId);

    void deleteItem(TrainingRecordItemId itemId);

    List<TrainingRecord> search(Long trainerId, Long memberId, Long exerciseId, LocalDate from, LocalDate to);

    TrainingRecord updateRecord(Long id, LocalDate date, Long trainerId, Long memberId, List<ItemInput> items);

    public record ItemInput(Long exerciseId, int sets, int reps, double weight) {}
}
