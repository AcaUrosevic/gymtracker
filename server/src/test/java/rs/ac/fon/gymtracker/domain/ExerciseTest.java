package rs.ac.fon.gymtracker.domain;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class ExerciseTest {

    private static Exercise validBase() {
        var e = new Exercise();
        e.setName("Bench Press");
        e.setEffort(1.25);
        return e;
    }

    private static boolean hasViolation(Set<ConstraintViolation<Exercise>> violations,
                                        String property, String messageFragment) {
        return violations.stream().anyMatch(v ->
                property.equals(v.getPropertyPath().toString()) &&
                        (v.getMessage() != null && v.getMessage().contains(messageFragment))
        );
    }

    private static String longString(int len) {
        return "x".repeat(len);
    }

    // =============== NAME ===============
    @Test
    void testSetName_ok() {
        var e = validBase();
        var violations = validator().validate(e);
        assertTrue(violations.isEmpty(), "Ne očekujem kršenja kada su polja validna");
        assertEquals("Bench Press", e.getName(), "Vrednost naziva mora biti upisana");
    }

    @Test
    void testSetName_null() {
        var e = new Exercise();
        e.setName(null);
        e.setEffort(1.0);
        var violations = validator().validate(e);

        assertFalse(violations.isEmpty());
        assertTrue(hasViolation(violations, "name", "must not be blank"),
                "Treba da postoji kršenje za 'name' zbog @NotBlank");
    }

    @Test
    void testSetName_blank() {
        var e = new Exercise();
        e.setName("  ");
        e.setEffort(1.0);
        var violations = validator().validate(e);

        assertFalse(violations.isEmpty());
        assertTrue(hasViolation(violations, "name", "must not be blank"));
    }

    @Test
    void testSetName_tooLong() {
        var e = new Exercise();
        e.setName(longString(81));
        e.setEffort(1.0);
        var violations = validator().validate(e);

        assertFalse(violations.isEmpty());
        assertTrue(hasViolation(violations, "name", "size must be between"),
                "Očekujem poruku za @Size na 'name'");
    }

    // =============== DESCRIPTION ===============
    @Test
    void testSetDescription_ok_or_null() {
        var e = validBase();
        e.setDescription(null);

        var violations = validator().validate(e);
        assertTrue(violations.isEmpty(), "Null description je dozvoljen");
        assertNull(e.getDescription());

        e.setDescription("Kratak opis");
        violations = validator().validate(e);
        assertTrue(violations.isEmpty(), "Kratak validan opis je dozvoljen");
        assertEquals("Kratak opis", e.getDescription());
    }

    @Test
    void testSetDescription_tooLong() {
        var e = validBase();
        e.setDescription(longString(256));


        var violations = validator().validate(e);
        assertFalse(violations.isEmpty());
        assertTrue(hasViolation(violations, "description", "size must be between"),
                "Očekujem poruku za @Size na 'description'");
    }

    // =============== EFFORT ===============
    @Test
    void testSetEffort_ok_bounds_and_value() {
        var e = new Exercise();
        e.setName("Row");

        e.setEffort(0.10);
        var v1 = validator().validate(e);
        assertTrue(v1.isEmpty(), "0.10 je dozvoljena donja granica");

        e.setEffort(5.00);
        var v2 = validator().validate(e);
        assertTrue(v2.isEmpty(), "5.00 je dozvoljena gornja granica");

        e.setEffort(1.75);
        var v3 = validator().validate(e);
        assertTrue(v3.isEmpty(), "Srednja vrednost u opsegu je dozvoljena");
        assertEquals(1.75, e.getEffort(), 1e-9);
    }

    @Test
    void testSetEffort_tooLow() {
        var e = new Exercise();
        e.setName("Press");
        e.setEffort(0.09);

        var violations = validator().validate(e);
        assertFalse(violations.isEmpty());
        assertTrue(hasViolation(violations, "effort", "must be greater than or equal to 0.10"),
                "Očekujem @DecimalMin poruku na 'effort'");
    }

    @Test
    void testSetEffort_tooHigh() {
        var e = new Exercise();
        e.setName("Lunge");
        e.setEffort(5.01);

        var violations = validator().validate(e);
        assertFalse(violations.isEmpty());
        assertTrue(hasViolation(violations, "effort", "must be less than or equal to 5.00"),
                "Očekujem @DecimalMax poruku na 'effort'");
    }
}
