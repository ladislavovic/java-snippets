package cz.kul.snippets.general.beanValidation;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeanValidation
{
    /*
### Useful validation annotations:

@NotNull
@AssertTrue
@Size validates that the annotated property value has a size between the attributes min and max. We can apply it to String, Collection, Map, and array properties.
@Min
@Max
@Email validates that the annotated property is a valid email address.
@NotEmpty validates that the property isn’t null or empty. We can apply it to String, Collection, Map or Array values.
@NotBlank can be applied only to text values, and validates that the property isn’t null or whitespace.
@Positive
@PositiveOrZero
@Negative
@NegativeOrZero
@Past
@PastOrPresent
@Future
@FutureOrPresent
     */


    private record GettingStartedRecord(
        @jakarta.validation.constraints.NotNull
        String name
    )
    {

    }

    @Test
    void gettingStarted()
    {
        // Creating of factory. The factory can create a validator.
        // messageInterpolator - do not know what this is, but without this setting it search some EL's classes on the classpath. So to use this configuration to avoid adding of more dependencies.
        ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator()) // I do not know what this is, but without this setting it search some EL's classes on the classpath. So to use this configuration to avoid adding of more dependencies.
            .buildValidatorFactory();

        // The Validator instance. it can validate a bean.
        Validator validator = factory.getValidator();

        {
            Set<ConstraintViolation<GettingStartedRecord>> violations = validator.validate(new GettingStartedRecord("Monica"));
            assertNoViolations(violations);
        }

        {
            assertViolationsContainsExpectedViolation(
                validator.validate(new GettingStartedRecord(null)),
                new ViolationData(PathImpl.createPathFromString("name"), "must not be null")
            );
        }
    }

    private record PropertyNotNull(@NotNull String name) {}

    @Test
    void propertyNotNullTest() {
        assertViolationsContainsExpectedViolation(
            createValidator().validate(new PropertyNotNull(null)),
            ViolationData.of("name", "must not be null")
        );
    }

    private record PropertyWithCustomMessage(@NotNull(message = "custom message") String name) {}

    @Test
    void propertyWithCustomMessageTest() {
        assertViolationsContainsExpectedViolation(
            createValidator().validate(new PropertyWithCustomMessage(null)),
            ViolationData.of("name", "custom message")
        );
    }

    private record CascadingMain(
        @Valid CascadingNested nested1,
        CascadingNested nested2
    )
    {

    }

    private record CascadingNested(
        @NotBlank String name
    )
    {

    }

    @Test void cascadingTest() {
        // Cascading is not automatic. Nested objects are not validated untill you mark
        // them mby annotation @Valid

        CascadingMain cascadingMain = new CascadingMain(
            new CascadingNested(null), // this is validation error
            new CascadingNested(null)  // this is NOT validation error
        );

        int numberOfViolations = createValidator().validate(cascadingMain).size();

        assertEquals(1, numberOfViolations);
    }


    private void assertNoViolations(Set<? extends ConstraintViolation<?>> violations) {
        assertTrue(violations.isEmpty());
    }

    private void assertViolationsContainsExpectedViolation(Set<? extends ConstraintViolation<?>> violations, ViolationData expectedViolationData)
    {
        for (ConstraintViolation<?> violation : violations) {
            if (expectedViolationData.match(violation)) {
                return;
            }
        }
        Assertions.fail("The violation was not found. All available violations: " + violations);
    }

    private record ViolationData(Path propertyPath, String message)
    {

        public static ViolationData of(String propertyPath, String message) {
            return new ViolationData(PathImpl.createPathFromString(propertyPath), message);
        }

        public boolean match(final ConstraintViolation<?> violation)
        {
            return violation.getPropertyPath().equals(propertyPath)
                   && violation.getMessage().equals(message);
        }

    }

    private Validator createValidator()
    {
        // Creating of factory. The factory can create a validator.
        // messageInterpolator - do not know what this is, but without this setting it search some EL's classes on the classpath. So to use this configuration to avoid adding of more dependencies.
        ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator()) // I do not know what this is, but without this setting it search some EL's classes on the classpath. So to use this configuration to avoid adding of more dependencies.
            .buildValidatorFactory();


        // The Validator instance. it can validate a bean.
        return factory.getValidator();
    }

}
