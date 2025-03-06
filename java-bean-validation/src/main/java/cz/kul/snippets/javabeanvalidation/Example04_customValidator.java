package cz.kul.snippets.javabeanvalidation;

import cz.kul.snippets.javabeanvalidation.common.ValidationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class Example04_customValidator extends ValidationTest
{
    @Test
    void customValidatorTest() {
        var names = new ArrayList<String>();
        names.add(null);
        names.add("monica");

        Person monica = new Person(
            names,
            new String[]{null}
        );

        int numberOfViolations = createValidator().validate(monica).size();

        assertThat(numberOfViolations).isEqualTo(2);
    }

    public record Person(
        @NoNullElements
        List<String> firstNames,

        @NoNullElements
        String[] ids
    )
    {

    }

    //
    // This is the annotation
    //
    @Constraint(validatedBy = NoNullElementsValidator.class) // the class that do the validation
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NoNullElements {

        String message() default "Collection or array must not contain null elements";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    //
    // This is the validator
    //
    public static class NoNullElementsValidator implements ConstraintValidator<NoNullElements, Object>
    {
        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return true; // Null collections/arrays are not checked (use @NotNull separately)
            }

            if (value instanceof Collection<?> collection) {
                return collection.stream().noneMatch(Objects::isNull);
            }

            if (value.getClass().isArray()) {
                int length = Array.getLength(value);
                for (int i = 0; i < length; i++) {
                    if (Array.get(value, i) == null) {
                        return false;
                    }
                }
            }

            return true; // Not a collection or array (not applicable)
        }
    }

}
