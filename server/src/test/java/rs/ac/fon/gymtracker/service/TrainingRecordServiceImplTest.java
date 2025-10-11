package rs.ac.fon.gymtracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.fon.gymtracker.domain.*;
import rs.ac.fon.gymtracker.repository.*;
import rs.ac.fon.gymtracker.service.impl.TrainingRecordServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingRecordServiceImplTest {

    @Mock TrainingRecordRepository recordRepo;
    @Mock TrainerRepository trainerRepo;
    @Mock MemberRepository memberRepo;
    @Mock ExerciseRepository exerciseRepo;
    @Mock TrainingRecordItemRepository itemRepo;

    @InjectMocks TrainingRecordServiceImpl service;

    @Test
    void createRecord_sets_zero_intensity_and_links_trainer_member() {
        var tr = new Trainer(); tr.setId(10L);
        var m  = new Member();  m.setId(20L);

        when(trainerRepo.findById(10L)).thenReturn(Optional.of(tr));
        when(memberRepo.findById(20L)).thenReturn(Optional.of(m));
        when(recordRepo.save(any(TrainingRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        var rec = service.createRecord(LocalDate.of(2025,1,15), 10L, 20L);

        assertEquals(LocalDate.of(2025,1,15), rec.getTrainingDate());
        assertSame(tr, rec.getTrainer());
        assertSame(m, rec.getMember());
        assertEquals(0.0, rec.getIntensity(), 0.0001);
    }

    @Test
    void addItem_assigns_next_rb_and_recalculates_intensity() {
        var rec = new TrainingRecord();
        rec.setId(100L);
        rec.setItems(new ArrayList<>());

        var existing = new TrainingRecordItem();
        existing.setId(new rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId(100L, 1));
        existing.setRecord(rec);
        var ex1 = new Exercise(); ex1.setEffort(1.0);
        existing.setExercise(ex1);
        existing.setSets(2); existing.setReps(10); existing.setWeight(50);
        rec.getItems().add(existing);

        var ex2 = new Exercise(); ex2.setId(300L); ex2.setEffort(1.5);

        when(recordRepo.findById(100L)).thenReturn(Optional.of(rec));
        when(exerciseRepo.findById(300L)).thenReturn(Optional.of(ex2));
        when(itemRepo.save(any(TrainingRecordItem.class))).thenAnswer(inv -> inv.getArgument(0));
        when(recordRepo.save(any(TrainingRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        var saved = service.addItem(100L, 300L, 3, 8, 40.0);

        assertEquals(2, saved.getId().getRb());
        assertSame(rec, saved.getRecord());
        assertSame(ex2, saved.getExercise());
        assertEquals(3, saved.getSets());
        assertEquals(8, saved.getReps());
        assertEquals(40.0, saved.getWeight(), 0.0001);

        assertEquals(2440.0, rec.getIntensity(), 0.0001);

        verify(itemRepo).save(any(TrainingRecordItem.class));
        verify(recordRepo, atLeastOnce()).save(rec);
    }

    @Test
    void recalcIntensity_sums_all_items() {
        var rec = new TrainingRecord();
        rec.setId(7L);

        var e1 = new Exercise(); e1.setEffort(1.2);
        var e2 = new Exercise(); e2.setEffort(0.9);

        var i1 = new TrainingRecordItem();
        i1.setExercise(e1); i1.setSets(4); i1.setReps(6); i1.setWeight(30.0); // 4*6*30*1.2 = 864
        var i2 = new TrainingRecordItem();
        i2.setExercise(e2); i2.setSets(2); i2.setReps(12); i2.setWeight(20.0); // 2*12*20*0.9 = 432

        rec.setItems(List.of(i1, i2));

        when(recordRepo.findById(7L)).thenReturn(Optional.of(rec));
        when(recordRepo.save(any(TrainingRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        var updated = service.recalcIntensity(7L);
        assertEquals(1296.0, updated.getIntensity(), 0.0001);
    }
}
