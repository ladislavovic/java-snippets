package cz.kul.snippets.java.collectors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class FrameworkCollectors
{

    private FrameworkCollectors()
    {
    }

    public static <MapKeyType, MapValueType> Collector<Map.Entry<MapKeyType, MapValueType>, ?, Map<MapKeyType, MapValueType>> toMapFromMapEntries()
    {
        return toMapFromMapEntries(HashMap::new, FrameworkCollectors::throwingMerger);
    }

    public static <MapKeyType, MapValueType> Collector<Map.Entry<MapKeyType, MapValueType>, ?, Map<MapKeyType, MapValueType>> toMapFromMapEntries(final BinaryOperator<MapValueType> mergeFunction)
    {
        return toMapFromMapEntries(HashMap::new, mergeFunction);
    }

    public static <MapKeyType, MapValueType, MapType extends Map<MapKeyType, MapValueType>> Collector<Map.Entry<MapKeyType, MapValueType>, ?, MapType> toMapFromMapEntries(final Supplier<MapType> mapSupplier)
    {
        return toMapFromMapEntries(mapSupplier, FrameworkCollectors::throwingMerger);
    }

    public static <MapKeyType, MapValueType, MapType extends Map<MapKeyType, MapValueType>> Collector<Map.Entry<MapKeyType, MapValueType>, ?, MapType> toMapFromMapEntries(
        final Supplier<MapType> mapSupplier,
        final BinaryOperator<MapValueType> mergeFunction
    )
    {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction, mapSupplier);
    }

    public static <StreamItem, MapKeyType, MapValueType> Collector<StreamItem, ?, IdentityHashMap<MapKeyType, MapValueType>> toIdentityHashMap(
        final Function<StreamItem, MapKeyType> keyMapper,
        final Function<StreamItem, MapValueType> valueMapper
    )
    {
        return Collectors.toMap(keyMapper, valueMapper, FrameworkCollectors::throwingMerger, IdentityHashMap::new);
    }

    /**
     * ()     -> null
     * (a)    -> a
     * (a, b) -> throw
     *
     * failWhenMoreThanOne()
     */
    public static <T> Collector<T, ?, T> toSingleton()
    {
        return Collectors.reducing(null, (oldValue, newValue) -> {
            if (oldValue != null) {
                throw new IllegalStateException(String.format("Conflict: %s and %s", oldValue, newValue));
            }

            return newValue;
        });
    }

    /**
     * ()     -> throw
     * (a)    -> a
     * (a, b) -> throw
     *
     *  exactlyOne()
     *
     *  onlyElement()
     *  failWhenNotOnly()
     */
    public static <T> Collector<T, ?, T> exactlyOne()
    {
        return Collectors.collectingAndThen(toSingleton(), val -> {
            if (val == null) {
                throw new IllegalStateException("No value found, exactly one expected");
            }

            return val;
        });
    }

    /**
     * ()     -> null
     * (a)    -> a
     * (a, b) -> null
     *
     * nullWhenNotExactlyOne()
     * nullWhenNotOnly()
     */
    public static <T> Collector<T, ?, T> exactlyOneOrNull()
    {
        var firstReduce = new AtomicBoolean(true);

        return Collectors.reducing(null, (oldValue, newValue) -> {
            if (firstReduce.get()) {
                firstReduce.set(false);
                return newValue;
            } else {
                return null;
            }
        });
    }

    public static <T> Collector<T, ?, Set<T>> duplicates()
    {
        return duplicatesBy(Function.identity());
    }

    public static <T, R> Collector<T, ?, Set<T>> duplicatesBy(final Function<T, R> transformation)
    {
        return Collectors.collectingAndThen(
            Collectors.groupingBy(transformation, Collectors.toList()),
            collected -> collected.values()
                .stream()
                .filter(it -> it.size() > 1)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    public static <T, I extends Comparable<I>> Collector<T, Object, List<T>> distinct(final Function<T, I> identifier)
    {
        return Collectors.collectingAndThen(
            Collectors.toCollection(() -> new TreeSet<T>(Comparator.comparing(identifier, Comparator.naturalOrder()))),
            ArrayList::new
        );
    }

    public static <T extends Comparable<? super T>> Collector<T, ?, NavigableSet<T>> toNavigableSet()
    {
        Comparator<T> comparator = Comparator.naturalOrder();
        return toNavigableSet(comparator);
    }

    public static <T> Collector<T, ?, NavigableSet<T>> toNavigableSet(final Comparator<T> comparator)
    {
        return Collectors.toCollection(() -> new TreeSet<>(comparator));
    }

    public static <T> T throwingMerger(
        final T u,
        final T v
    )
    {
        throw new IllegalStateException(String.format("Duplicate key for objects [%s] and [%s]", u, v));
    }

}
