package rs.ac.fon.gymtracker.domain;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class TrainerTest {
    private static Trainer validBase() {
        var t = new Trainer();
        t.setFirstName("Marko");
        t.setLastName("Markovic");
        t.setUsername("mmarko");
        t.setEmail("marko@example.com");
        t.setPasswordHash("$2a$10$qwerty...");
        return t;
    }

    private static boolean hasViolation(Set<ConstraintViolation<Trainer>> v,
                                        String property, String messageFragment) {
        return v.stream().anyMatch(cv ->
                property.equals(cv.getPropertyPath().toString())
                        && cv.getMessage() != null
                        && cv.getMessage().contains(messageFragment)
        );
    }

    private static String longString(int len) { return "x".repeat(len); }

    // --------- firstName (NotBlank, max 60) ---------
    @Test
    void setFirstName_ok() {
        var t = validBase();
        t.setFirstName("Ana");
        var res = validator().validate(t);
        assertTrue(res.isEmpty());
        assertEquals("Ana", t.getFirstName());
    }

    @Test
    void setFirstName_null_violates_NotBlank() {
        var t = validBase();
        t.setFirstName(null);
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "firstName", "must not be blank"));
    }
    @Test
    void setFirstName_blank_violates_NotBlank() {
        var t = validBase();
        t.setFirstName("   ");
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "firstName", "must not be blank"));
    }

    @Test
    void setFirstName_tooLong_violates_Size60() {
        var t = validBase();
        t.setFirstName(longString(61));
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "firstName", "size must be between"));
    }

    // --------- lastName (NotBlank, max 60) ---------
    @Test
    void setLastName_ok() {
        var t = validBase();
        t.setLastName("Jovanovic");
        var res = validator().validate(t);
        assertTrue(res.isEmpty());
        assertEquals("Jovanovic", t.getLastName());
    }

    @Test
    void setLastName_null_violates_NotBlank() {
        var t = validBase();
        t.setLastName(null);
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "lastName", "must not be blank"));
    }

    @Test
    void setLastName_blank_violates_NotBlank() {
        var t = validBase();
        t.setLastName(" ");
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "lastName", "must not be blank"));
    }

    @Test
    void setLastName_tooLong_violates_Size60() {
        var t = validBase();
        t.setLastName(longString(61));
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "lastName", "size must be between"));
    }

    // --------- email (optional, @Email, max 120) ---------
    @Test
    void setEmail_null_ok() {
        var t = validBase();
        t.setEmail(null);
        var res = validator().validate(t);
        assertTrue(res.isEmpty());
        assertNull(t.getEmail());
    }

    @Test
    void setEmail_valid_ok() {
        var t = validBase();
        t.setEmail("ana.jovanovic@fit.rs");
        var res = validator().validate(t);
        assertTrue(res.isEmpty());
        assertEquals("ana.jovanovic@fit.rs", t.getEmail());
    }

    @Test
    void setEmail_invalid_format_violates_Email() {
        var t = validBase();
        t.setEmail("not-an-email");
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "email", "must be a well-formed email address"));
    }

    @Test
    void setEmail_tooLong_violates_Size120() {
        var t = validBase();
        t.setEmail(longString(121));
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "email", "size must be between"));
    }

    // --------- username (NotBlank, max 60) ---------
    @Test
    void setUsername_ok() {
        var t = validBase();
        t.setUsername("trainer1");
        var res = validator().validate(t);
        assertTrue(res.isEmpty());
        assertEquals("trainer1", t.getUsername());
    }

    @Test
    void setUsername_null_violates_NotBlank() {
        var t = validBase();
        t.setUsername(null);
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "username", "must not be blank"));
    }

    @Test
    void setUsername_blank_violates_NotBlank() {
        var t = validBase();
        t.setUsername("   ");
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "username", "must not be blank"));
    }

    @Test
    void setUsername_tooLong_violates_Size60() {
        var t = validBase();
        t.setUsername(longString(61));
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "username", "size must be between"));
    }

    // --------- passwordHash (optional, max 255) ---------
    @Test
    void setPasswordHash_null_ok() {
        var t = validBase();
        t.setPasswordHash(null);
        var res = validator().validate(t);
        assertTrue(res.isEmpty());
        assertNull(t.getPasswordHash());
    }

    @Test
    void setPasswordHash_ok() {
        var t = validBase();
        t.setPasswordHash("$2a$10$abcdefghijklmnopqrstuv");
        var res = validator().validate(t);
        assertTrue(res.isEmpty());
        assertEquals("$2a$10$abcdefghijklmnopqrstuv", t.getPasswordHash());
    }

    @Test
    void setPasswordHash_tooLong_violates_Size255() {
        var t = validBase();
        t.setPasswordHash(longString(256));
        var res = validator().validate(t);
        assertFalse(res.isEmpty());
        assertTrue(hasViolation(res, "passwordHash", "size must be between"));
    }
}
