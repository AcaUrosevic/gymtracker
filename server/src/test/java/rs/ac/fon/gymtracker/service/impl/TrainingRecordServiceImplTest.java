package rs.ac.fon.gymtracker.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.fon.gymtracker.domain.*;
import rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId;
import rs.ac.fon.gymtracker.repository.*;
import rs.ac.fon.gymtracker.service.TrainingRecordService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingRecordServiceImplTest {

    @Mock private TrainingRecordRepository repo;
    @Mock private TrainerRepository trainerRepo;
    @Mock private MemberRepository memberRepo;
    @Mock private ExerciseRepository exerciseRepo;
    @Mock private TrainingRecordItemRepository itemRepo;

    @InjectMocks private TrainingRecordServiceImpl service;

    private Trainer trainer;
    private Member member;
    private Exercise exercise;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setFirstName("Marko");

        member = new Member();
        member.setId(2L);
        member.setFirstName("Jovan");

        exercise = new Exercise();
        exercise.setId(3L);
        exercise.setEffort(1.5);
    }

    @Test
    void testCreateRecord() {
        var date = LocalDate.of(2023, 5, 10);
        var record = new TrainingRecord();
        record.setId(100L);
        record.setTrainingDate(date);
        record.setTrainer(trainer);
        record.setMember(member);

        when(trainerRepo.findById(1L)).thenReturn(Optional.of(trainer));
        when(memberRepo.findById(2L)).thenReturn(Optional.of(member));
        when(repo.save(any())).thenReturn(record);

        var out = service.createRecord(date, 1L, 2L);

        assertEquals(100L, out.getId());
        assertEquals(date, out.getTrainingDate());
        assertEquals(trainer, out.getTrainer());
        assertEquals(member, out.getMember());

        verify(repo).save(any());
    }


    @Test
    void testRecalcIntensity() {
        var item = new TrainingRecordItem();
        item.setSets(2);
        item.setReps(10);
        item.setWeight(50);
        item.setExercise(exercise);

        var record = new TrainingRecord();
        record.setId(10L);
        record.setItems(List.of(item));

        when(repo.findById(10L)).thenReturn(Optional.of(record));
        when(repo.save(record)).thenReturn(record);

        var out = service.recalcIntensity(10L);

        assertEquals(1500.0, out.getIntensity());

        verify(repo).save(record);
    }

    @Test
    void testDeleteRecord() {
        service.deleteRecord(99L);
        verify(repo).deleteById(99L);
    }

    @Test
    void testDeleteItemTriggersRecalc() {
        var itemId = new TrainingRecordItemId(10L, 1);

        when(repo.findById(10L)).thenReturn(Optional.of(new TrainingRecord()));
        when(repo.save(any())).thenReturn(new TrainingRecord());

        service.deleteItem(itemId);

        verify(itemRepo).deleteById(itemId);
        verify(repo).save(any());
    }

    @Test
    void testListByMember() {
        var list = List.of(new TrainingRecord(), new TrainingRecord());
        when(repo.findByMemberIdOrderByTrainingDateDesc(2L)).thenReturn(list);

        var out = service.listByMember(2L);
        assertEquals(2, out.size());
        verify(repo).findByMemberIdOrderByTrainingDateDesc(2L);
    }

    @Test
    void testListByTrainer() {
        var list = List.of(new TrainingRecord());
        when(repo.findByTrainerIdOrderByTrainingDateDesc(1L)).thenReturn(list);

        var out = service.listByTrainer(1L);
        assertEquals(1, out.size());
        verify(repo).findByTrainerIdOrderByTrainingDateDesc(1L);
    }


    @Test
    void testUpdateRecord() {
        var rec = new TrainingRecord();
        rec.setId(10L);
        rec.setItems(new ArrayList<>());

        when(repo.findById(10L)).thenReturn(Optional.of(rec));
        when(trainerRepo.findById(1L)).thenReturn(Optional.of(trainer));
        when(memberRepo.findById(2L)).thenReturn(Optional.of(member));
        when(exerciseRepo.findById(3L)).thenReturn(Optional.of(exercise));
        when(repo.save(any())).thenReturn(rec);

        var input = List.of(new TrainingRecordService.ItemInput(3L, 2, 10, 50.0));
        var out = service.updateRecord(10L, LocalDate.of(2023, 5, 10), 1L, 2L, input);

        assertEquals(1, out.getItems().size());
        assertEquals(1500.0, out.getIntensity());
        assertEquals(trainer, out.getTrainer());
        assertEquals(member, out.getMember());

        verify(repo).save(rec);
    }
}