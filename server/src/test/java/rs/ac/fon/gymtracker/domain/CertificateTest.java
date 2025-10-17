package rs.ac.fon.gymtracker.domain;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class CertificateTest {
    private static Certificate validBase() {
        var c = new Certificate();
        c.setName("ISSA CPT");
        c.setType("Strength");
        return c;
    }

    private static boolean hasViolation(Set<ConstraintViolation<Certificate>> v,
                                        String property, String messageFragment) {
        return v.stream().anyMatch(cv ->
                property.equals(cv.getPropertyPath().toString())
                        && cv.getMessage() != null
                        && cv.getMessage().contains(messageFragment)
        );
    }

    private static String longString(int len) { return "x".repeat(len); }

    // --------- name ---------

    @Test
    void setName_ok() {
        var c = validBase();
        c.setName("USAW Lv1");
        var result = validator().validate(c);
        assertTrue(result.isEmpty(), "No violations expected");
        assertEquals("USAW Lv1", c.getName());
    }

    @Test
    void setName_null_should_violate_NotBlank() {
        var c = validBase();
        c.setName(null);
        var result = validator().validate(c);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "name", "must not be blank"));
    }

    @Test
    void setName_blank_should_violate_NotBlank() {
        var c = validBase();
        c.setName("   ");
        var result = validator().validate(c);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "name", "must not be blank"));
    }

    @Test
    void setName_tooLong_should_violate_Size_max120() {
        var c = validBase();
        c.setName(longString(121)); // > 120
        var result = validator().validate(c);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "name", "size must be between"));
    }

    // --------- type ---------

    @Test
    void setType_ok() {
        var c = validBase();
        c.setType("Nutrition");
        var result = validator().validate(c);
        assertTrue(result.isEmpty(), "No violations expected");
        assertEquals("Nutrition", c.getType());
    }

    @Test
    void setType_null_should_violate_NotBlank() {
        var c = validBase();
        c.setType(null);
        var result = validator().validate(c);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "type", "must not be blank"));
    }

    @Test
    void setType_blank_should_violate_NotBlank() {
        var c = validBase();
        c.setType("  ");
        var result = validator().validate(c);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "type", "must not be blank"));
    }

    @Test
    void setType_tooLong_should_violate_Size_max80() {
        var c = validBase();
        c.setType(longString(81)); // > 80
        var result = validator().validate(c);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "type", "size must be between"));
    }
}
