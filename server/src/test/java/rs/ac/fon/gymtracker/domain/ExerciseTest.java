package rs.ac.fon.gymtracker.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class ExerciseTest {
    @Test
    void valid_object_has_no_violations() {
        var e = new Exercise();
        e.setName("Bench Press");
        e.setEffort(1.25);
        assertTrue(validator().validate(e).isEmpty());
    }
    @Test
    void invalid_when_name_blank_or_effort_out_of_range() {
        var e = new Exercise();
        e.setEffort(9.99);
        assertFalse(validator().validate(e).isEmpty());
    }
}
