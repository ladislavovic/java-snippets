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

@SpringBootTest
public class Example02_customMessage extends ValidationTest
{

    @Test
    void propertyWithCustomMessageTest() {
        assertViolationsContainsExpectedViolation(
            createValidator().validate(new Person(null)),
            ViolationData.of("name", "You MUST enter a name dude")
        );
    }

    public record Person(
        @NotNull(message = "You MUST enter a name dude")
        String name
    )
    {

    }

}
