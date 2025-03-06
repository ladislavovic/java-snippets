package cz.kul.snippets.javabeanvalidation;

import cz.kul.snippets.javabeanvalidation.common.ValidationTest;
import cz.kul.snippets.javabeanvalidation.common.ViolationData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class Example03_nestedBeanValidation extends ValidationTest
{

    @Test void cascadingTest() {
        // Validation of nested beans is not automatic.
        // Nested objects are not validated until you mark them mby annotation @Valid

        Person monica = new Person(
            "Monica",
            new Person.Address("Hlavni 1", null), // this IS a validation error
            new Person.Address("Hlavni 1", null)  // this IS NOT a validation error
        );

        int numberOfViolations = createValidator().validate(monica).size();

        assertThat(numberOfViolations).isEqualTo(1);
    }

    public record Person(
        @NotNull
        String name,

        @Valid // this annotation cause the nested bean is also validated
        @NotNull
        Address postalAddress,

        @NotNull
        Address invoiceAddress
    )
    {

        public record Address(
            @NotNull
            String addressLine,

            @NotNull
            String town
        )
        {

        }

    }

}
