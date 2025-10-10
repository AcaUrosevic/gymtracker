package rs.ac.fon.gymtracker.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.fon.gymtracker.testutil.ValidationUtil.validator;

class ServicePackageTest {

    @Test
    void valid_object_has_no_violations() {
        ServicePackage p = new ServicePackage();
        p.setName("Monthly");
        p.setDescription("30 days");
        p.setDurationDays(30);
        p.setPrice(3000);
        assertTrue(validator().validate(p).isEmpty());
    }

    @Test
    void invalid_when_name_blank_or_bad_numbers() {
        ServicePackage p = new ServicePackage();
        p.setDurationDays(0);
        p.setPrice(-1);
        assertFalse(validator().validate(p).isEmpty());
    }
}
