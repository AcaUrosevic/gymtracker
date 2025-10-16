package rs.ac.fon.gymtracker.domain;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class TrainingRecordTest {
    private static TrainingRecord baseValid() {
        var tr = new TrainingRecord();
        tr.setTrainingDate(LocalDate.now());
        tr.setIntensity(0.0);
        tr.setTrainer(minTrainer());
        tr.setMember(minMember());
        return tr;
    }

    private static Trainer minTrainer() {
        var t = new Trainer();
        t.setFirstName("M");
        t.setLastName("L");
        t.setUsername("m");
        return t;
    }
    private static Member minMember() {
        var m = new Member();
        m.setFirstName("P");
        m.setLastName("P");
        return m;
    }

    private static boolean hasViolation(Set<ConstraintViolation<TrainingRecord>> v,
                                        String property, String messageFragment) {
        return v.stream().anyMatch(cv ->
                property.equals(cv.getPropertyPath().toString()) &&
                        cv.getMessage() != null &&
                        cv.getMessage().contains(messageFragment)
        );
    }

    // --- trainingDate ---
    @Test
    void setTrainingDate_ok() {
        var tr = baseValid();
        tr.setTrainingDate(LocalDate.of(2025, 1, 15));
        var res = validator().validate(tr);
        assertTrue(res.isEmpty());
        assertEquals(LocalDate.of(2025, 1, 15), tr.getTrainingDate());
    }

    @Test
    void setTrainingDate_null_violates_NotNull() {
        var tr = baseValid();
        tr.setTrainingDate(null);
        var res = validator().validate(tr);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "trainingDate", "must not be null"));
    }

    // --- intensity (>= 0.00) ---
    @Test
    void setIntensity_ok_nonNegative() {
        var tr = baseValid();
        tr.setIntensity(123.45);
        var res = validator().validate(tr);
        assertTrue(res.isEmpty());
        assertEquals(123.45, tr.getIntensity(), 1e-9);
    }

    @Test
    void setIntensity_negative_violates_DecimalMin() {
        var tr = baseValid();
        tr.setIntensity(-0.01);
        var res = validator().validate(tr);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "intensity", "must be greater than or equal to 0.00"));
    }

    // --- trainer (@NotNull) ---
    @Test
    void setTrainer_ok() {
        var tr = baseValid();
        var t = minTrainer();
        tr.setTrainer(t);
        var res = validator().validate(tr);
        assertTrue(res.isEmpty());
        assertSame(t, tr.getTrainer());
    }

    @Test
    void setTrainer_null_violates_NotNull() {
        var tr = baseValid();
        tr.setTrainer(null);
        var res = validator().validate(tr);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "trainer", "must not be null"));
    }

    // --- member (@NotNull) ---
    @Test
    void setMember_ok() {
        var tr = baseValid();
        var m = minMember();
        tr.setMember(m);
        var res = validator().validate(tr);
        assertTrue(res.isEmpty());
        assertSame(m, tr.getMember());
    }

    @Test
    void setMember_null_violates_NotNull() {
        var tr = baseValid();
        tr.setMember(null);
        var res = validator().validate(tr);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "member", "must not be null"));
    }
}
