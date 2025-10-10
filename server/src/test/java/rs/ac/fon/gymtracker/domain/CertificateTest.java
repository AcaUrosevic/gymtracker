package rs.ac.fon.gymtracker.domain;

import jakarta.validation.*;
import org.junit.jupiter.api.*;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class CertificateTest {
    static Validator validator;
    @BeforeAll static void setup() { validator = Validation.buildDefaultValidatorFactory().getValidator(); }

    @Test void valid_certificate() {
        Certificate c = new Certificate();
        c.setName("ISSA CPT");
        c.setType("Strength");
        Set<ConstraintViolation<Certificate>> v = validator.validate(c);
        assertTrue(v.isEmpty());
    }

    @Test void invalid_blank_name() {
        Certificate c = new Certificate();
        c.setName(" ");
        c.setType("Strength");
        var v = validator.validate(c);
        assertFalse(v.isEmpty());
    }
}
