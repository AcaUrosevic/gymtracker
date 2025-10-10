package rs.ac.fon.gymtracker.testutil;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

/** Jednostavan pristup Bean Validatoru u testovima. */
public final class ValidationUtil {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    public static Validator validator() { return VALIDATOR; }
    private ValidationUtil() {}
}
