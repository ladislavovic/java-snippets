package cz.kul.snippets.xodus.example01_environment;


import com.google.common.base.Stopwatch;
import jetbrains.exodus.bindings.LongBinding;
import jetbrains.exodus.bindings.StringBinding;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.EnvironmentConfig;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.Store;
import jetbrains.exodus.env.StoreConfig;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Xodus has three API how to work with data - Environment, Entity Store, VFS.
 * This example use "Envoronment API"
 *
 * Write speed is about 300K items/s.
 *
 * Random read speed is about 200K items/s when store without key prefixes.
 *
 * Random read speed is about 500K items/s when store with key prefixes.
 *
 */
public class XodusEnvironment {

	public static void main(String[] args) throws IOException  {
		final long NO_OF_ITEMS = 500_000;
		final long NO_OF_ITEMS_READ = 50_000;

		EnvironmentConfig envCfg = new EnvironmentConfig();
		envCfg.setLogDurableWrite(false);
		try (Environment env = Environments.newInstance(
				"/tmp/xodus-environment-" + RandomStringUtils.randomAlphabetic(5),
				envCfg)) {

			// Writing
			{
				Stopwatch sw = Stopwatch.createStarted();
				writeToEnvironment(env, NO_OF_ITEMS);
				sw.stop();
				long timeMs = sw.elapsed(TimeUnit.MILLISECONDS);
				double itemsPerSecond = (NO_OF_ITEMS / (double) timeMs) * 1000;
				System.out.println("" + NO_OF_ITEMS + " were written in " + timeMs + " ms (" + itemsPerSecond + " items/s).");
			}

			// Reading
			{
				List<Long> ids = new ArrayList<>((int) NO_OF_ITEMS_READ);
				long step = NO_OF_ITEMS / NO_OF_ITEMS_READ;
				for (int i = 0; i < NO_OF_ITEMS_READ; i++) {
					ids.add(i * step);
				}
				Collections.shuffle(ids);
				long[] idsArr = new long[(int) NO_OF_ITEMS_READ];
				for (int i = 0; i < NO_OF_ITEMS_READ; i++) {
					idsArr[i] = ids.get(i);
				}
				Stopwatch sw = Stopwatch.createStarted();
				env.executeInReadonlyTransaction(txn -> {
					Store store = env.openStore("Messages", StoreConfig.USE_EXISTING, txn);
					for (int i = 0; i < NO_OF_ITEMS_READ; i++) {
						long id = idsArr[i];
						store.get(txn, LongBinding.longToEntry(id));
					}
				});
				sw.stop();
				long timeMs = sw.elapsed(TimeUnit.MILLISECONDS);
				double itemsPerSecond = (NO_OF_ITEMS_READ / (double) timeMs) * 1000;
				System.out.println("" + NO_OF_ITEMS_READ + " were randomly read in " + timeMs + " ms (" + itemsPerSecond + " items/s).");
			}
		}
	}

	private static void writeToEnvironment(Environment env, long noOfItems) {
		final int bufferSize = 1000;
		final int recordSize = 32;
		final int maxIndex = bufferSize - recordSize;

		String buffer = RandomStringUtils.randomAlphabetic(1000);
		Random random = new Random();

		env.executeInTransaction(txn -> {
			final Store store = env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES_WITH_PREFIXING, txn);
			for (long i = 0; i < noOfItems; i++) {
				int start = random.nextInt(maxIndex);
				String subStr = buffer.substring(start);
				store.put(txn, LongBinding.longToEntry(i), StringBinding.stringToEntry(subStr));
			}
		});
	}


}
