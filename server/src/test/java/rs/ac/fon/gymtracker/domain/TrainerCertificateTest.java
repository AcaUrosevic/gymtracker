package rs.ac.fon.gymtracker.domain;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class TrainerCertificateTest {

    private static Trainer trainer() {
        var t = new Trainer();
        t.setFirstName("Milan");
        t.setLastName("Milić");
        t.setUsername("milan");
        return t;
    }

    private static Certificate cert() {
        var c = new Certificate();
        c.setName("ISSA CPT");
        c.setType("Strength");
        return c;
    }

    private static TrainerCertificate validBase() {
        var tc = new TrainerCertificate();
        tc.setTrainer(trainer());
        tc.setCertificate(cert());
        tc.setIssuedAt(LocalDate.now());
        return tc;
    }

    private static boolean hasViolation(Set<? extends ConstraintViolation<?>> v,
                                        String property, String msgFragment) {
        return v.stream().anyMatch(cv ->
                property.equals(cv.getPropertyPath().toString())
                        && cv.getMessage() != null
                        && cv.getMessage().contains(msgFragment)
        );
    }

    // --- scenariji ---

    @Test
    void valid_ok() {
        var tc = validBase();
        var result = validator().validate(tc);
        assertTrue(result.isEmpty(), "No violations expected");
        assertNotNull(tc.getTrainer());
        assertNotNull(tc.getCertificate());
        assertNotNull(tc.getIssuedAt());
    }

    @Test
    void trainer_null_should_violate_NotNull() {
        var tc = validBase();
        tc.setTrainer(null);
        var result = validator().validate(tc);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "trainer", "must not be null"));
    }

    @Test
    void certificate_null_should_violate_NotNull() {
        var tc = validBase();
        tc.setCertificate(null);
        var result = validator().validate(tc);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "certificate", "must not be null"));
    }

    @Test
    void issuedAt_null_is_allowed() {
        var tc = validBase();
        tc.setIssuedAt(null);
        var result = validator().validate(tc);
        assertTrue(result.isEmpty(), "issuedAt is optional");
        assertNull(tc.getIssuedAt());
    }

    @Test
    void issuedAt_future_should_violate_PastOrPresent() {
        var tc = validBase();
        tc.setIssuedAt(LocalDate.now().plusDays(2));
        var result = validator().validate(tc);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "issuedAt", "must be a date in the past or in the present"));
    }
}
