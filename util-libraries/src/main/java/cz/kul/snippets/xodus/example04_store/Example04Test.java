package cz.kul.snippets.xodus.example04_store;

import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.bindings.LongBinding;
import jetbrains.exodus.bindings.StringBinding;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.EnvironmentConfig;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.Store;
import jetbrains.exodus.env.StoreConfig;
import jetbrains.exodus.env.Transaction;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Stores
 *
 * <ul>

 *
 * </ul>
 *
 *
 */
public class Example04Test {

	@Test
	public void testTransactions_canNotCommitBecauseOfDbVersionMismatch() {
		final String STORE1 = "store1";
		final String STORE2 = "store2";
		ArrayByteIterable key = LongBinding.longToEntry(1L);
		try (Environment env = createEnv()) {
			Transaction t1 = env.beginTransaction();
			Store t1Store = env.openStore(STORE1, StoreConfig.WITHOUT_DUPLICATES, t1);
			t1Store.put(t1, key, StringBinding.stringToEntry("hi"));

			Transaction t2 = env.beginTransaction();
			Store t2Store = env.openStore(STORE2, StoreConfig.WITHOUT_DUPLICATES, t2);
			t2Store.put(t2, key, StringBinding.stringToEntry("hello"));

			Assert.assertTrue(t2.commit());
			Assert.assertFalse(t1.commit()); // can not commit, because DB version was increased by t2 commit
			t1.abort();
		}
	}


	private Environment createEnv() {
		EnvironmentConfig envCfg = new EnvironmentConfig();
		return Environments.newInstance(
				"/tmp/xodus-environment-" + RandomStringUtils.randomAlphabetic(5),
				envCfg);
	}

}
