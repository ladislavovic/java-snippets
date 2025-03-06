package cz.kul.snippets.javabeanvalidation.common;

import org.hibernate.validator.internal.engine.path.PathImpl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;

public record ViolationData(Path propertyPath, String message)
{

    public static ViolationData of(String propertyPath, String message)
    {
        return new ViolationData(PathImpl.createPathFromString(propertyPath), message);
    }

    public boolean match(final ConstraintViolation<?> violation)
    {
        return violation.getPropertyPath().equals(propertyPath)
               && violation.getMessage().equals(message);
    }

}
