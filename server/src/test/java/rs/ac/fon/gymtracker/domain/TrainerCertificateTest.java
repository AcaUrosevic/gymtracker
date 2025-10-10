package rs.ac.fon.gymtracker.domain;

import jakarta.validation.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TrainerCertificateTest {
    static Validator validator;
    @BeforeAll static void setup() { validator = Validation.buildDefaultValidatorFactory().getValidator(); }

    @Test void valid_link() {
        var t = new Trainer(); t.setFirstName("Milan"); t.setLastName("Milić"); t.setUsername("milan");
        var c = new Certificate(); c.setName("ISSA CPT"); c.setType("Strength");
        var tc = new TrainerCertificate();
        tc.setTrainer(t); tc.setCertificate(c); tc.setIssuedAt(LocalDate.now());
        assertTrue(validator.validate(tc).isEmpty());
    }

    @Test void invalid_future_date() {
        var t = new Trainer(); t.setFirstName("Milan"); t.setLastName("Milić"); t.setUsername("milan");
        var c = new Certificate(); c.setName("ISSA CPT"); c.setType("Strength");
        var tc = new TrainerCertificate();
        tc.setTrainer(t); tc.setCertificate(c); tc.setIssuedAt(LocalDate.now().plusDays(3));
        assertFalse(validator.validate(tc).isEmpty());
    }
}
