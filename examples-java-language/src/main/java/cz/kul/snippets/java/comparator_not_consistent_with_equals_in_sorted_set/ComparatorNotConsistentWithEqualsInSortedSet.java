package cz.kul.snippets.java.comparator_not_consistent_with_equals_in_sorted_set;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * When you use {@link java.util.SortedSet} with custom comparator, the elements
 * {@code equals()} method <b>does not have to be consistent</b> with the comparator.
 * Because {@link java.util.SortedSet} does not use {@code equal()} method at all, it uses
 * {@code comparator.compare()} method.
 * </p>
 *
 * <p>
 * But you must be aware that it breaks {@link java.util.Set} interface contract. It say
 * that the {@link java.util.Set} can not contains two elements which are equals. But
 * when you use {@link java.util.SortedSet} and {@code compare()} and {@code equals()} are
 * not consistent you can easily break this rule.
 * </p>
 *
 * <p>
 * Anyway when you are OK with that, you can use this approach.
 * </p>
 *
 */
public class ComparatorNotConsistentWithEqualsInSortedSet {

	@Test
	public void comparatorNotConsistentWithEqualsInSortedSet() {
		var set = new TreeSet<Element>(Comparator.comparing(Element::getName));

		// Both elements are in the set even though they are equal by equals() method
		set.add(new Element(1, "monica"));
		set.add(new Element(1, "rachel"));
		assertEquals(2, set.size());

		// When I remove elements it again compares elements by compare() method
		assertTrue(set.remove(new Element(1, "monica")));
		assertFalse(set.remove(new Element(1, "monica")));
		assertEquals(1, set.size());
	}

	private static class Element {
		long id;

		String name;

		public Element(long id, String name) {
			this.id = id;
			this.name = name;
		}

		public long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Element element = (Element) o;
			return id == element.id;
		}

		@Override
		public int hashCode() {
			return Objects.hash(id);
		}
	}

}
