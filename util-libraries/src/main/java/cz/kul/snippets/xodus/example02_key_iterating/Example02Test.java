package cz.kul.snippets.xodus.example02_key_iterating;

import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.bindings.LongBinding;
import jetbrains.exodus.bindings.StringBinding;
import jetbrains.exodus.env.Cursor;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.EnvironmentConfig;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.Store;
import jetbrains.exodus.env.StoreConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.io.IOException;


public class Example02Test {

	@Test
	public void test() throws IOException {
		// Prepare environment
		EnvironmentConfig envCfg = new EnvironmentConfig();
		envCfg.setLogDurableWrite(false);
		Environment env = Environments.newInstance(
				"/tmp/xodus-environment-" + RandomStringUtils.randomAlphabetic(5),
				envCfg);

		// Write to the Store
		int NO_OF_ITEMS = 10;
		env.executeInTransaction(txn -> {
			final Store store = env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES, txn);
			for (long i = 0; i < NO_OF_ITEMS; i++) {
				store.put(txn, LongBinding.longToEntry(i), StringBinding.stringToEntry("value_" + i));
			}
		});

		// Reading all items by Cursor
		env.executeInTransaction(txn -> {
			final Store store = env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES, txn);
			try (Cursor cursor = store.openCursor(txn)) {
				while (cursor.getNext()) {
					ByteIterable key = cursor.getKey();
					ByteIterable value = cursor.getValue();
					System.out.println("Key= " + LongBinding.entryToLong(key) + ", value=" + StringBinding.entryToString(value));
				}
			}
		});
	}

}
