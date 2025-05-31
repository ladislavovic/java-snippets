package cz.kul.snippets;

import org.slf4j.Logger;

import java.util.Objects;

public class Console
{
    public static void logAndAssert(
        final String title,
        final String valueName,
        final Object actualValue,
        final Object expectedValue,
        final Logger logger
    )
    {
        if (!Objects.equals(actualValue, expectedValue)) {
            String errMsg = "\"%s\" failed. ActualValue=%s, expectedValue=%s".formatted(valueName, actualValue, expectedValue);
            throw new AssertionError(errMsg);
        }

        String indentation = "    ";
        StringBuilder message = new StringBuilder();
        message.append("\n\n");
        message.append("%s%s:\n".formatted(indentation, title));
        message.append("%s%s: %s\n".formatted(indentation, valueName, actualValue));

        logger.info(message.toString());
    }

}
