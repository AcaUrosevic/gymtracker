package rs.ac.fon.gymtracker.domain;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class MemberTest {
    private static Member validBase() {
        var m = new Member();
        m.setFirstName("Petar");
        m.setLastName("Petrović");
        return m;
    }

    private static boolean hasViolation(Set<ConstraintViolation<Member>> violations,
                                        String property, String messageFragment) {
        return violations.stream().anyMatch(v ->
                property.equals(v.getPropertyPath().toString())
                        && v.getMessage() != null
                        && v.getMessage().contains(messageFragment)
        );
    }

    private static String longString(int len) {
        return "x".repeat(len);
    }

    // ---------- firstName ----------
    @Test
    void setFirstName_ok() {
        var m = validBase();
        m.setFirstName("Milan");
        var result = validator().validate(m);
        assertTrue(result.isEmpty(), "No violations expected");
        assertEquals("Milan", m.getFirstName(), "Value must be stored");
    }

    @Test
    void setFirstName_null_should_violate_NotBlank() {
        var m = validBase();
        m.setFirstName(null);
        var result = validator().validate(m);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "firstName", "must not be blank"));
    }

    @Test
    void setFirstName_blank_should_violate_NotBlank() {
        var m = validBase();
        m.setFirstName("   ");
        var result = validator().validate(m);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "firstName", "must not be blank"));
    }

    @Test
    void setFirstName_tooLong_should_violate_Size_max60() {
        var m = validBase();
        m.setFirstName(longString(61));
        var result = validator().validate(m);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "firstName", "size must be between"));
    }

    // ---------- lastName ----------
    @Test
    void setLastName_ok() {
        var m = validBase();
        m.setLastName("Ivić");
        var result = validator().validate(m);
        assertTrue(result.isEmpty());
        assertEquals("Ivić", m.getLastName());
    }

    @Test
    void setLastName_null_should_violate_NotBlank() {
        var m = validBase();
        m.setLastName(null);
        var result = validator().validate(m);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "lastName", "must not be blank"));
    }

    @Test
    void setLastName_blank_should_violate_NotBlank() {
        var m = validBase();
        m.setLastName("  ");
        var result = validator().validate(m);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "lastName", "must not be blank"));
    }

    @Test
    void setLastName_tooLong_should_violate_Size_max60() {
        var m = validBase();
        m.setLastName(longString(61));
        var result = validator().validate(m);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "lastName", "size must be between"));
    }

    // ---------- email (optional) ----------
    @Test
    void email_null_is_allowed() {
        var m = validBase();
        m.setEmail(null);
        var result = validator().validate(m);
        assertTrue(result.isEmpty());
        assertNull(m.getEmail());
    }

    @Test
    void email_valid_is_ok() {
        var m = validBase();
        m.setEmail("petar@example.com");
        var result = validator().validate(m);
        assertTrue(result.isEmpty());
        assertEquals("petar@example.com", m.getEmail());
    }

    @Test
    void email_invalid_format_should_violate_Email() {
        var m = validBase();
        m.setEmail("not-an-email");
        var result = validator().validate(m);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "email", "must be a well-formed email address"));
    }

    @Test
    void email_tooLong_should_violate_Size_max120() {
        var m = validBase();
        m.setEmail(longString(121) + "@ex.com"); // > 120 ukupno
        var result = validator().validate(m);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "email", "size must be between"));
    }

    // ---------- servicePackage (optional) ----------
    @Test
    void servicePackage_null_is_allowed() {
        var m = validBase();
        m.setServicePackage(null);
        var result = validator().validate(m);
        assertTrue(result.isEmpty());
        assertNull(m.getServicePackage());
    }

    @Test
    void servicePackage_set_is_ok() {
        var m = validBase();
        var pkg = new ServicePackage();
        pkg.setId(1L);
        pkg.setName("Monthly");
        pkg.setDurationDays(30);
        pkg.setPrice(3000);
        m.setServicePackage(pkg);

        var result = validator().validate(m);
        assertTrue(result.isEmpty());
        assertNotNull(m.getServicePackage());
        assertEquals("Monthly", m.getServicePackage().getName());
    }
}
