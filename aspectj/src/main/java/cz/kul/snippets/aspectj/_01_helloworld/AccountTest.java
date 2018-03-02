package cz.kul.snippets.aspectj._01_helloworld;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * terms
 *
 * woven
 * poincut
 * aspect
 * join point
 * advice
 *
 * crosscutting concerns - I understand it as problem, which is spread over an application
 *
 * A join point is a well-defined point in the program flow.
 *
 * A pointcut picks out certain join points and values at those points.
 *
 * A piece of advice is code that is executed when a join point is reached.
 *
 * AspectJ's aspect are the unit of modularity
 *
 * Each method call at runtime is a different join point, even if it comes from the same call expression in the program.
 *
 * what happen when aspect call method which has another aspect assign? Does it works?
 *
 * TODO: I ended before Advice chapter
 *
 */
public class AccountTest {
	private Account account;

	@Before
	public void before() {
		account = new Account();
	}

	@Test
	public void given20AndMin10_whenWithdraw5_thenSuccess() {
		assertTrue(account.withdraw(5));
	}

	@Test
	public void given20AndMin10_whenWithdraw100_thenFail() {
		assertFalse(account.withdraw(100));
	}
}
