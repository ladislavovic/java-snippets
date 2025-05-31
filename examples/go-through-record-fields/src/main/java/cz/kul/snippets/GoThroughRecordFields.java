package cz.kul.snippets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GoThroughRecordFields
{

    public static void main(String[] args) throws JsonProcessingException
    {
        Book book = new Book(
            "Harry Potter",
            "123456789012",
            Set.of("fantasy", "kids", "british"),
            new BigDecimal("10.00"),
            new Book.Author("JK", "Rowling"),
            List.of(
                new Book.Chapter("Chapter 1", 10),
                new Book.Chapter("Chapter 2", 15)
            )
        );

        var goThroughRecordFields = new GoThroughRecordFields();
        List<Node> nodes = goThroughRecordFields.iterateFieldsRecursivelly(book, "");

        ObjectMapper objectMapper = new ObjectMapper();
        String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodes);
        System.out.println(output);

    }

    public List<Node> iterateFieldsRecursivelly(Object object, String pathPrefix)
    {
        ArrayList<Node> results = new ArrayList<>();

        for (Field field : object.getClass().getDeclaredFields()) {

            if (!field.canAccess(object)) {
                continue;
            }

            Object fieldValue;
            try {
                fieldValue = field.get(object);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }

            String fieldName = field.getName();
            Class<?> fieldType = field.getType();

            if (shouldGoRecursivelly(fieldType)) {
                if (fieldValue != null) {
                    results.addAll(iterateFieldsRecursivelly(fieldValue, pathPrefix + "/" + fieldName));
                }
                continue;
            }

            if (fieldValue instanceof List listValue) {
                for (int i = 0; i < listValue.size(); i++) {
                    results.addAll(iterateFieldsRecursivelly(listValue.get(i), pathPrefix + "/" + fieldName + "[" + i + "]"));
                }
                continue;
            }

            String schemaName = Optional.ofNullable(field.getAnnotation(Schema.class))
                .map(Schema::name)
                .orElse(null);

            results.add(new Node(
                fieldName,
                schemaName,
                pathPrefix + "/" + fieldName,
                fieldValue,
                fieldType.getName()
            ));
        }
        return results;
    }

    public boolean shouldGoRecursivelly(Class<?> clazz)
    {
        return isTheClassNestedRecursivelly(Book.class, clazz);
    }

    public boolean isTheClassNestedRecursivelly(Class<?> nesting, Class<?> inner)
    {
        Class<?> declaringClass = inner;
        do {
            if (declaringClass == nesting) {
                return true;
            }
            declaringClass = declaringClass.getDeclaringClass();
        } while (declaringClass != null);

        return false;
    }

    public record Book(

        @Schema(name = "Title")
        String title,

        String ean,

        Set<String> categories,

        BigDecimal price,

        Author author,

        @ArraySchema
        List<Chapter> chapters
    )
    {

        public record Author(
            String firstName,
            String secondName
        )
        {

        }

        public record Chapter(
            String title,
            int pages
        )
        {

        }

    }

    public record Node(
        String name,
        String schemaName,
        String path,
        Object value,
        String type
    )
    {

    }

}

