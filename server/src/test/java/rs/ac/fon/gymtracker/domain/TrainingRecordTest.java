package rs.ac.fon.gymtracker.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class TrainingRecordTest {
    private Trainer trainer() { var t = new Trainer(); t.setFirstName("M"); t.setLastName("L"); t.setUsername("m"); return t; }
    private Member member() { var m = new Member(); m.setFirstName("P"); m.setLastName("P"); return m; }

    @Test
    void valid_object_has_no_violations() {
        var tr = new TrainingRecord();
        tr.setTrainingDate(LocalDate.now());
        tr.setIntensity(0.0);
        tr.setTrainer(trainer());
        tr.setMember(member());
        assertTrue(validator().validate(tr).isEmpty());
    }
    @Test
    void invalid_when_missing_date_or_refs() {
        var tr = new TrainingRecord();
        assertFalse(validator().validate(tr).isEmpty());
    }
}
