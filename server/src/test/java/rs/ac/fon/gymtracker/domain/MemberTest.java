package rs.ac.fon.gymtracker.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class MemberTest {
    @Test
    void valid_object_has_no_violations() {
        var m = new Member();
        m.setFirstName("Petar");
        m.setLastName("Petrović");
        m.setEmail("petar@example.com");
        assertTrue(validator().validate(m).isEmpty());
    }
    @Test
    void invalid_when_required_names_missing() {
        var m = new Member();
        assertFalse(validator().validate(m).isEmpty());
    }
}
