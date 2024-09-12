package cz.kul.snippets.java.collectors;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CustomCollectorTest
{

    @Test
    public void toSingletonTest() {
        assertThat(Stream.of().collect(FrameworkCollectors.toSingleton())).isNull();
        assertThat(Stream.of(1).collect(FrameworkCollectors.toSingleton())).isEqualTo(1);
        assertThatThrownBy(() -> {
            Stream.of(1, 2).collect(FrameworkCollectors.toSingleton());
        }).isExactlyInstanceOf(IllegalStateException.class);
    }

    @Test
    public void exactlyOneTest() {
        assertThatThrownBy(() -> {
            Stream.of().collect(FrameworkCollectors.exactlyOne());
        }).isExactlyInstanceOf(IllegalStateException.class);

        assertThat(Stream.of(1).collect(FrameworkCollectors.exactlyOne())).isEqualTo(1);

        assertThatThrownBy(() -> {
            Stream.of(1, 2).collect(FrameworkCollectors.exactlyOne());
        }).isExactlyInstanceOf(IllegalStateException.class);
    }

    @Test
    public void exactlyOneOrNullTest() {
        assertThat(Stream.of().collect(FrameworkCollectors.exactlyOneOrNull())).isNull();
        assertThat(Stream.of(1).collect(FrameworkCollectors.exactlyOneOrNull())).isEqualTo(1);
        assertThat(Stream.of(1, 2).collect(FrameworkCollectors.exactlyOneOrNull())).isNull();
        assertThat(Stream.of(1, 2, 3).collect(FrameworkCollectors.exactlyOneOrNull())).isNull();
    }

    @Test
    public void exactlyOneWithMultipleItems()
    {
        var data = List.of(1, 2, 3);

        assertThatThrownBy(() -> {
            data.stream().collect(FrameworkCollectors.exactlyOne());
        }).isExactlyInstanceOf(IllegalStateException.class);
    }

    @Test
    public void exactlyOne()
    {
        int value = 1;
        var data = List.of(value);

        assertThat(data.stream().collect(FrameworkCollectors.exactlyOne())).isEqualTo(value);
    }

    @Test
    public void exactlyOneWithZeroItems()
    {
        var data = List.of(); // empty

        assertThatThrownBy(() -> {
            data.stream().collect(FrameworkCollectors.exactlyOne());
        }).isExactlyInstanceOf(IllegalStateException.class);
    }

    @Test
    public void exactlyOneOrNull_singleItem()
    {
        int value = 1;
        var data = List.of(value);

        assertThat(data.stream().collect(FrameworkCollectors.exactlyOneOrNull())).isEqualTo(value);
    }

    @Test
    public void exactlyOneOrNull_multipleItems()
    {
        var data = List.of(1, 2);

        assertThat(data.stream().collect(FrameworkCollectors.exactlyOneOrNull())).isNull();
    }

    @Test
    public void exactlyOneOrNull_noItems()
    {
        var data = List.of();

        assertThat(data.stream().collect(FrameworkCollectors.exactlyOneOrNull())).isNull();
    }

    @Test
    public void duplicatesBy()
    {
        var data = List.of("a", "b", "aa", "bbb");

        assertThat(data.stream().collect(FrameworkCollectors.duplicatesBy(String::length))).containsExactly("a", "b");
    }


}
