package cz.kul.snippets.javabeanvalidation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.kul.snippets.javabeanvalidation.common.ValidationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class Example05_jacksonAndValidation extends ValidationTest
{
    @Test
    void customValidatorTest() throws JsonProcessingException
    {
        // Jackson does not validate during deserialization.
        //
        // If you want to do it, you must prepare custom deserializer that do the validation.
        //
        // Example:

        var input = """
            {
                "name": null
            }
            """;
        var objectMapper = new ObjectMapper();
        Person person = objectMapper.readValue(input, Person.class);

        // It passes, no exception is thrown

    }

    @Validated
    public record Person(
        @NotNull
        String name
    )
    {

    }

}
