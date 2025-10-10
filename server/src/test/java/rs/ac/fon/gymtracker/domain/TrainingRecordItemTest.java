package rs.ac.fon.gymtracker.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrainingRecordItemTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    private Trainer validTrainer() {
        Trainer t = new Trainer();
        t.setFirstName("Marko");
        t.setLastName("Marković");
        t.setUsername("marko");
        return t;
    }

    private Member validMember() {
        Member m = new Member();
        m.setFirstName("Petar");
        m.setLastName("Petrović");
        return m;
    }

    private Exercise validExercise() {
        Exercise e = new Exercise();
        e.setName("Bench Press");
        e.setDescription("Chest press");
        e.setEffort(1.20);
        return e;
    }

    private TrainingRecord validRecord() {
        TrainingRecord tr = new TrainingRecord();
        tr.setTrainingDate(LocalDate.of(2025, 1, 15));
        tr.setIntensity(0.0);
        tr.setTrainer(validTrainer());
        tr.setMember(validMember());
        return tr;
    }

    @Test
    void valid_item_passes_validation() {
        TrainingRecordItem item = new TrainingRecordItem();

        item.setId(new TrainingRecordItemId(1L, 1));

        item.setRecord(validRecord());
        item.setExercise(validExercise());

        item.setSets(4);
        item.setReps(8);
        item.setWeight(60.0);

        Set<ConstraintViolation<TrainingRecordItem>> violations = validator.validate(item);
        assertTrue(violations.isEmpty());
    }

    @Test
    void invalid_item_fails_validation_for_sets_reps_weight() {
        TrainingRecordItem item = new TrainingRecordItem();

        item.setId(new TrainingRecordItemId(1L, 2));
        item.setRecord(validRecord());
        item.setExercise(validExercise());

        item.setSets(0);
        item.setReps(0);
        item.setWeight(-5.0);

        Set<ConstraintViolation<TrainingRecordItem>> violations = validator.validate(item);

        assertFalse(violations.isEmpty(), "Očekujemo validacione greške");
        assertTrue(violations.size() >= 3, "Očekujemo >= 3 greške (sets, reps, weight)");
    }
}
