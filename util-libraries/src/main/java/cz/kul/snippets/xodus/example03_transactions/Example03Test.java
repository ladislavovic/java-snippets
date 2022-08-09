package cz.kul.snippets.xodus.example03_transactions;

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
 * <ul>
 *   <li>Transaction provides snapshot isolation.</li>
 *   <li>There are also exclusive transactions</li>
 *   <li>
 *       Transaction flush - it is like commit but the transaction remains opened.
 *       Flushed changes are not removed when you revert or abort the transaction.
 *   </li>
 *   <li>
 *     revert() - remove all (unflushed) transaction changes and switch to the newest data version. So it
 *     drops all your changes you done in transaction. If you want them you must repeat them.
 *   </li>
 *   <li>
 *     When you open a store, you provide a store configuration and transaction. It is used
 *     when the store does not exist yet. If you know the store exists already,
 *     use StoreConfig.USE_EXISTING. The existing store can by used my more transactions.
 *   </li>
 *   <li>
 *     Store with key prefixes is better for random access, without it is better for
 *     sequential access.
 *   </li>
 *   <li>
 *     You can not close a store. Close always the environment. Store.close() is deprecated.
 *   </li>
 * </ul>
 */
public class Example03Test {

	@Test
	public void testTransactions_transactionsShouldBeIsolated() {
		final String STORE_NAME = "messages";
		ArrayByteIterable key = LongBinding.longToEntry(1L);
		try (Environment env = createEnv()) {
			Transaction t1 = env.beginTransaction();
			Store t1Store = env.openStore(STORE_NAME, StoreConfig.WITHOUT_DUPLICATES, t1);
			t1Store.put(t1, key, StringBinding.stringToEntry("hi"));

			Transaction t2 = env.beginTransaction();
			Store t2Store = env.openStore(STORE_NAME, StoreConfig.WITHOUT_DUPLICATES, t2);

			Assert.assertNotNull(t1Store.get(t1, key));
			Assert.assertNull(t2Store.get(t2, key));

			t1.commit();
			t2.abort();
		}
	}

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

	@Test
	public void testTransactions_flushedChangesCanNotBeRemovedByAbortOrRevert() {
		final String STORE1 = "store1";
		ArrayByteIterable key1 = LongBinding.longToEntry(1L);
		ArrayByteIterable key2 = LongBinding.longToEntry(2L);
		ArrayByteIterable key3 = LongBinding.longToEntry(3L);
		try (Environment env = createEnv()) {
			// flush and abort
			{
				Transaction t = env.beginTransaction();
				Store store = env.openStore(STORE1, StoreConfig.WITHOUT_DUPLICATES, t);
				store.put(t, key1, StringBinding.stringToEntry("hi"));
				t.flush();
				t.abort();
			}

			// flush and revert
			{
				Transaction t = env.beginTransaction();
				Store store = env.openStore(STORE1, StoreConfig.WITHOUT_DUPLICATES, t);
				store.put(t, key2, StringBinding.stringToEntry("hello"));
				t.flush();
				t.revert();
				t.abort();
			}

			// read it
			{
				Transaction t = env.beginReadonlyTransaction();
				Store store = env.openStore(STORE1, StoreConfig.WITHOUT_DUPLICATES, t);
				Assert.assertNotNull(store.get(t, key1));
				Assert.assertNotNull(store.get(t, key2));
				Assert.assertNull(store.get(t, key3));
				t.abort();
			}
		}

	}


	@Test
	public void testTransactions_storeCanBeSharedByTransactions() {
		final String STORE_NAME = "messages";
		ArrayByteIterable key = LongBinding.longToEntry(1L);
		try (Environment env = createEnv()) {
			// create store
			Transaction tx = env.beginTransaction();
			Store t1Store = env.openStore(STORE_NAME, StoreConfig.WITHOUT_DUPLICATES, tx);
			tx.commit();

			Transaction tx2 = env.beginTransaction();
			Store store = env.openStore(STORE_NAME, StoreConfig.USE_EXISTING, tx2);
			tx2.abort();

			env.executeInTransaction(txn -> {
				store.put(txn, LongBinding.longToEntry(1L), StringBinding.stringToEntry("aa"));
			});
			env.executeInTransaction(txn -> {
				store.put(txn, LongBinding.longToEntry(2L), StringBinding.stringToEntry("bb"));
			});
			env.executeInTransaction(txn -> {
				Assert.assertNotNull(store.get(txn, LongBinding.longToEntry(1L)));
				Assert.assertNotNull(store.get(txn, LongBinding.longToEntry(2L)));
				Assert.assertNull(store.get(txn, LongBinding.longToEntry(3L)));
			});
		}
	}

	private Environment createEnv() {
		EnvironmentConfig envCfg = new EnvironmentConfig();
		return Environments.newInstance(
				"/tmp/xodus-environment-" + RandomStringUtils.randomAlphabetic(5),
				envCfg);
	}

}
