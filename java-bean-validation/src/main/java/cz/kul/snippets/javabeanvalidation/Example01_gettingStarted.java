package cz.kul.snippets.javabeanvalidation;

import cz.kul.snippets.javabeanvalidation.common.ValidationTest;
import cz.kul.snippets.javabeanvalidation.common.ViolationData;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class Example01_gettingStarted extends ValidationTest
{

    @Test
    void gettingStarted()
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
        Validator validator = factory.getValidator();

        {
            Set<ConstraintViolation<Person>> violations = validator.validate(new Person("Monica"));
            assertNoViolations(violations);
        }

        {
            assertViolationsContainsExpectedViolation(
                validator.validate(new Person(null)),
                new ViolationData(PathImpl.createPathFromString("name"), "must not be null")
            );
        }
    }

    public record Person(
        @NotNull
        String name
    )
    {

    }

}
