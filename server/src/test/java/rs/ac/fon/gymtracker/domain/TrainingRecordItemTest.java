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

    private static Trainer validTrainer() {
        var t = new Trainer();
        t.setFirstName("Marko");
        t.setLastName("Marković");
        t.setUsername("marko");
        return t;
    }

    private static Member validMember() {
        var m = new Member();
        m.setFirstName("Petar");
        m.setLastName("Petrović");
        return m;
    }

    private static Exercise validExercise() {
        var e = new Exercise();
        e.setName("Bench Press");
        e.setDescription("Chest press");
        e.setEffort(1.20);
        return e;
    }

    private static TrainingRecord validRecord() {
        var tr = new TrainingRecord();
        tr.setTrainingDate(LocalDate.of(2025, 1, 15));
        tr.setIntensity(0.0);
        tr.setTrainer(validTrainer());
        tr.setMember(validMember());
        return tr;
    }

    private static TrainingRecordItem validBase() {
        var item = new TrainingRecordItem();
        item.setId(new TrainingRecordItemId(10L, 1));
        item.setRecord(validRecord());
        item.setExercise(validExercise());
        item.setSets(4);
        item.setReps(8);
        item.setWeight(60.0);
        return item;
    }

    private static boolean hasViolation(Set<? extends ConstraintViolation<?>> v,
                                        String property, String messageFragment) {
        return v.stream().anyMatch(cv ->
                property.equals(cv.getPropertyPath().toString())
                        && cv.getMessage() != null
                        && cv.getMessage().contains(messageFragment)
        );
    }

    // ---------- scenariji ----------

    @Test
    void valid_ok() {
        var item = validBase();
        var result = validator.validate(item);
        assertTrue(result.isEmpty(), "No violations expected");
        assertEquals(4, item.getSets());
        assertEquals(8, item.getReps());
        assertEquals(60.0, item.getWeight());
        assertNotNull(item.getRecord());
        assertNotNull(item.getExercise());
    }

    @Test
    void record_null_should_violate_NotNull() {
        var item = validBase();
        item.setRecord(null);
        var result = validator.validate(item);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "record", "must not be null"));
    }

    @Test
    void exercise_null_should_violate_NotNull() {
        var item = validBase();
        item.setExercise(null);
        var result = validator.validate(item);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "exercise", "must not be null"));
    }

    @Test
    void sets_min_ok_boundary_1() {
        var item = validBase();
        item.setSets(1);
        var result = validator.validate(item);
        assertTrue(result.isEmpty());
        assertEquals(1, item.getSets());
    }

    @Test
    void sets_zero_should_violate_Min1() {
        var item = validBase();
        item.setSets(0);
        var result = validator.validate(item);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "sets", "must be greater than or equal to 1"));
    }

    @Test
    void reps_min_ok_boundary_1() {
        var item = validBase();
        item.setReps(1);
        var result = validator.validate(item);
        assertTrue(result.isEmpty());
        assertEquals(1, item.getReps());
    }

    @Test
    void reps_zero_should_violate_Min1() {
        var item = validBase();
        item.setReps(0);
        var result = validator.validate(item);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "reps", "must be greater than or equal to 1"));
    }

    @Test
    void weight_ok_boundary_zero() {
        var item = validBase();
        item.setWeight(0.0);
        var result = validator.validate(item);
        assertTrue(result.isEmpty());
        assertEquals(0.0, item.getWeight());
    }

    @Test
    void weight_negative_should_violate_DecimalMin_0() {
        var item = validBase();
        item.setWeight(-5.0);
        var result = validator.validate(item);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "weight", "must be greater than or equal to 0.0"));
    }
}
