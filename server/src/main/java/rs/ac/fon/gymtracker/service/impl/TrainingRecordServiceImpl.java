package rs.ac.fon.gymtracker.service.impl;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.fon.gymtracker.domain.*;
import rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId;
import rs.ac.fon.gymtracker.repository.*;
import rs.ac.fon.gymtracker.service.TrainingRecordService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TrainingRecordServiceImpl
        extends AbstractJpaCrudService<TrainingRecord, Long, TrainingRecordRepository>
        implements TrainingRecordService {

    private final TrainerRepository trainerRepo;
    private final MemberRepository memberRepo;
    private final ExerciseRepository exerciseRepo;
    private final TrainingRecordItemRepository itemRepo;

    public TrainingRecordServiceImpl(TrainingRecordRepository repo,
                                     TrainerRepository trainerRepo,
                                     MemberRepository memberRepo,
                                     ExerciseRepository exerciseRepo,
                                     TrainingRecordItemRepository itemRepo) {
        super(repo);
        this.trainerRepo = trainerRepo;
        this.memberRepo = memberRepo;
        this.exerciseRepo = exerciseRepo;
        this.itemRepo = itemRepo;
    }

    @Override
    protected TrainingRecord doMergeAndSave(TrainingRecord current, TrainingRecord patch) {
        if (patch.getTrainingDate() != null) current.setTrainingDate(patch.getTrainingDate());
        if (patch.getTrainer() != null) current.setTrainer(patch.getTrainer());
        if (patch.getMember() != null)  current.setMember(patch.getMember());
        return repo.save(current);
    }

    @Override
    public TrainingRecord createRecord(LocalDate date, Long trainerId, Long memberId) {
        var tr = trainerRepo.findById(trainerId).orElseThrow();
        var m  = memberRepo.findById(memberId).orElseThrow();
        var rec = new TrainingRecord();
        rec.setTrainingDate(date);
        rec.setTrainer(tr);
        rec.setMember(m);
        rec.setIntensity(0.0);
        return repo.save(rec);
    }

    @Override
    public TrainingRecordItem addItem(Long recordId, Long exerciseId, int sets, int reps, double weight) {
        var rec = repo.findById(recordId).orElseThrow();
        var ex  = exerciseRepo.findById(exerciseId).orElseThrow();

        int nextRb = rec.getItems().size() + 1;

        var item = new TrainingRecordItem();
        item.setId(new rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId(rec.getId(), nextRb));
        item.setRecord(rec);
        item.setExercise(ex);
        item.setSets(sets);
        item.setReps(reps);
        item.setWeight(weight);

        var saved = itemRepo.save(item);
        rec.getItems().add(saved);

        recalcIntensity(recordId);
        return saved;
    }

    @Override
    public TrainingRecord recalcIntensity(Long recordId) {
        var rec = repo.findById(recordId).orElseThrow();
        double sum = rec.getItems().stream()
                .mapToDouble(i -> i.getSets() * i.getReps() * i.getWeight() * i.getExercise().getEffort())
                .sum();
        rec.setIntensity(sum);
        return repo.save(rec);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingRecord> listByMember(Long memberId) {
        return repo.findByMemberIdOrderByTrainingDateDesc(memberId);
    }



    @Override
    @Transactional(readOnly = true)
    public List<TrainingRecord> listByTrainer(Long trainerId) {
        return repo.findByTrainerIdOrderByTrainingDateDesc(trainerId);
    }

    @Override
    public void deleteRecord(Long recordId) {
        repo.deleteById(recordId);
    }

    @Override
    public void deleteItem(TrainingRecordItemId itemId) {
        itemRepo.deleteById(itemId);
        var recId = itemId.getRecordId();
        recalcIntensity(recId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingRecord> search(Long trainerId,
                                       Long memberId,
                                       Long exerciseId,
                                       LocalDate from,
                                       LocalDate to) {

        final List<Specification<TrainingRecord>> andSpecs = new ArrayList<>();

        if (trainerId != null) {
            andSpecs.add((root, query, cb) -> cb.equal(root.get("trainer").get("id"), trainerId));
        }
        if (memberId != null) {
            andSpecs.add((root, query, cb) -> cb.equal(root.get("member").get("id"), memberId));
        }
        if (from != null) {
            andSpecs.add((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("trainingDate"), from));
        }
        if (to != null) {
            andSpecs.add((root, query, cb) -> cb.lessThanOrEqualTo(root.get("trainingDate"), to));
        }
        if (exerciseId != null) {
            andSpecs.add((root, query, cb) -> {
                assert query != null;
                var sub =   query.subquery(Long.class);
                var item = sub.from(TrainingRecordItem.class);
                sub.select(item.get("id").get("recordId"))
                        .where(
                                cb.equal(item.get("id").get("recordId"), root.get("id")),
                                cb.equal(item.get("exercise").get("id"), exerciseId)
                        );
                return cb.exists(sub);
            });
        }

        if (andSpecs.isEmpty()) {
            var all = repo.findAll();
            all.sort((a, b) -> b.getTrainingDate().compareTo(a.getTrainingDate()));
            return all;
        }

        var spec = Specification.allOf(andSpecs);
        var list = repo.findAll(spec);
        list.sort((a, b) -> b.getTrainingDate().compareTo(a.getTrainingDate()));
        return list;
    }

    @Override
    public TrainingRecord updateRecord(Long id,
                                       LocalDate date,
                                       Long trainerId,
                                       Long memberId,
                                       List<ItemInput> items) {
        var rec = repo.findById(id).orElseThrow();

        var tr = trainerRepo.findById(trainerId).orElseThrow();
        var m  = memberRepo.findById(memberId).orElseThrow();
        rec.setTrainingDate(date);
        rec.setTrainer(tr);
        rec.setMember(m);

        rec.getItems().clear();

        int rb = 1;
        if (items != null) {
            for (var it : items) {
                var ex = exerciseRepo.findById(it.exerciseId()).orElseThrow();

                var item = new TrainingRecordItem();
                item.setId(new TrainingRecordItemId(rec.getId(), rb++));
                item.setRecord(rec);
                item.setExercise(ex);
                item.setSets(it.sets());
                item.setReps(it.reps());
                item.setWeight(it.weight());

                rec.getItems().add(item);
            }
        }

        double sum = rec.getItems().stream()
                .mapToDouble(i -> i.getSets() * i.getReps() * i.getWeight() * i.getExercise().getEffort())
                .sum();
        rec.setIntensity(sum);

        var saved = repo.save(rec);
        return repo.findById(saved.getId()).orElseThrow();
    }
}
