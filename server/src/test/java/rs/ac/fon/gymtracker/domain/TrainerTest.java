package rs.ac.fon.gymtracker.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class TrainerTest {
    @Test
    void valid_object_has_no_violations() {
        var t = new Trainer();
        t.setFirstName("Marko");
        t.setLastName("Marković");
        t.setUsername("mmarko");
        t.setEmail("marko@example.com");
        assertTrue(validator().validate(t).isEmpty());
    }
    @Test
    void invalid_when_required_fields_missing() {
        var t = new Trainer();
        assertFalse(validator().validate(t).isEmpty());
    }
}
