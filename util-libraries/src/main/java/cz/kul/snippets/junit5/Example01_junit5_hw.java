package cz.kul.snippets.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Example01_junit5_hw {

	@Test
	public void test1() {
		assertEquals(10, 10);
	}

	@Test
	public void assertAllExample() {
		// It executes all assertions even though  some of them fails
		assertAll("numbers",
				() -> assertEquals(1, 0),
				() -> assertEquals(2, 0),
				() -> assertEquals(3, 0));
	}

	@Test
	public void exceptionExample() {
		Throwable ex = assertThrows(NullPointerException.class, () -> ((Object) null).toString());
	}

}
