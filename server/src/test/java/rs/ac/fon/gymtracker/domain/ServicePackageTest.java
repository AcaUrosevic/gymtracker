package rs.ac.fon.gymtracker.domain;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class ServicePackageTest {
    private static ServicePackage validBase() {
        var p = new ServicePackage();
        p.setName("Monthly");
        p.setDescription("30 days");
        p.setDurationDays(30);
        p.setPrice(3000);
        return p;
    }

    private static boolean hasViolation(Set<ConstraintViolation<ServicePackage>> v,
                                        String property, String messageFragment) {
        return v.stream().anyMatch(cv ->
                property.equals(cv.getPropertyPath().toString())
                        && cv.getMessage() != null
                        && cv.getMessage().contains(messageFragment)
        );
    }

    private static String longString(int len) {
        return "x".repeat(len);
    }

    // --------- name ---------
    @Test
    void setName_ok() {
        var p = validBase();
        p.setName("Quarterly");
        var result = validator().validate(p);
        assertTrue(result.isEmpty(), "No violations expected");
        assertEquals("Quarterly", p.getName());
    }

    @Test
    void setName_null_should_violate_NotBlank() {
        var p = validBase();
        p.setName(null);
        var result = validator().validate(p);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "name", "must not be blank"));
    }

    @Test
    void setName_blank_should_violate_NotBlank() {
        var p = validBase();
        p.setName("   ");
        var result = validator().validate(p);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "name", "must not be blank"));
    }

    @Test
    void setName_tooLong_should_violate_Size_max80() {
        var p = validBase();
        p.setName(longString(81));
        var result = validator().validate(p);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "name", "size must be between"));
    }

    // --------- description (optional) ---------
    @Test
    void description_null_is_allowed() {
        var p = validBase();
        p.setDescription(null);
        var result = validator().validate(p);
        assertTrue(result.isEmpty());
        assertNull(p.getDescription());
    }

    @Test
    void description_ok() {
        var p = validBase();
        p.setDescription("Access to all classes");
        var result = validator().validate(p);
        assertTrue(result.isEmpty());
        assertEquals("Access to all classes", p.getDescription());
    }

    @Test
    void description_tooLong_should_violate_Size_max255() {
        var p = validBase();
        p.setDescription(longString(256));
        var result = validator().validate(p);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "description", "size must be between"));
    }

    // --------- durationDays (≥ 1) ---------
    @Test
    void durationDays_ok_boundary_1() {
        var p = validBase();
        p.setDurationDays(1);
        var result = validator().validate(p);
        assertTrue(result.isEmpty());
        assertEquals(1, p.getDurationDays());
    }

    @Test
    void durationDays_zero_should_violate_Min1() {
        var p = validBase();
        p.setDurationDays(0);
        var result = validator().validate(p);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "durationDays", "must be greater than or equal to 1"));
    }

    @Test
    void durationDays_negative_should_violate_Min1() {
        var p = validBase();
        p.setDurationDays(-10);
        var result = validator().validate(p);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "durationDays", "must be greater than or equal to 1"));
    }

    // --------- price (≥ 0) ---------
    @Test
    void price_ok_boundary_0() {
        var p = validBase();
        p.setPrice(0);
        var result = validator().validate(p);
        assertTrue(result.isEmpty());
        assertEquals(0, p.getPrice());
    }

    @Test
    void price_positive_ok() {
        var p = validBase();
        p.setPrice(1999);
        var result = validator().validate(p);
        assertTrue(result.isEmpty());
        assertEquals(1999, p.getPrice());
    }

    @Test
    void price_negative_should_violate_Min0() {
        var p = validBase();
        p.setPrice(-1);
        var result = validator().validate(p);
        assertFalse(result.isEmpty());
        assertTrue(hasViolation(result, "price", "must be greater than or equal to 0"));
    }
}
