package cz.kul.snippets.javabeanvalidation.common;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Assertions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationTest
{

    protected void assertNoViolations(Set<? extends ConstraintViolation<?>> violations)
    {
        assertTrue(violations.isEmpty());
    }

    protected void assertViolationsContainsExpectedViolation(Set<? extends ConstraintViolation<?>> violations, ViolationData expectedViolationData)
    {
        for (ConstraintViolation<?> violation : violations) {
            if (expectedViolationData.match(violation)) {
                return;
            }
        }
        Assertions.fail("The violation was not found. All available violations: " + violations);
    }

    protected Validator createValidator()
    {
        // Creating of factory
        // * The factory can create a validator.
        // * messageInterpolator - do not know what this is, but without this setting
        //   it search some EL's classes on the classpath. So to use this configuration
        //   to avoid adding of more dependencies.
        ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();

        // The Validator instance. it can validate a bean.
        return factory.getValidator();
    }

}
