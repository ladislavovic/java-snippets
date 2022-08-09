package cz.kul.snippets.spring.example_26_configFileReloading;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FileChangeListener {

	/** The whole file path */
	private final String filePath;

	/** The path to thi directory where the file is */
	private final String dirPath;

	/** Name of the file without directory path */
	private final String fileName;

	/** The action which is executed when the file is changed */
	private final Consumer<WatchEvent<?>> actionOnFileChange;

	public static FileChangeListener createAndListen(String filePath, Consumer<WatchEvent<?>> actionOnFileChange) {
		FileChangeListener fileChangeListener = new FileChangeListener(filePath, actionOnFileChange);
		execute(fileChangeListener);
		return fileChangeListener;
	}

	private static void execute(FileChangeListener listener) {
		Thread t = new Thread(listener::run);
		t.setDaemon(true);
		t.start();
	}

	private FileChangeListener(String filePath, Consumer<WatchEvent<?>> actionOnFileChange) {
		validateFilePath(filePath);
		this.filePath = filePath;
		this.fileName = getFileName(filePath);
		this.dirPath = getDirPath(filePath);
		this.actionOnFileChange = actionOnFileChange;
	}

	private void run() {
		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Paths.get(dirPath).register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

			WatchKey key;
			while ((key = watchService.take()) != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					if (event.context().toString().equals(fileName)) {
						actionOnFileChange.accept(event);
					}
				}
				key.reset();
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InterruptedException e) {
			// nothing to do, it just quit the run method and finish this thread
		}
	}

	private void validateFilePath(String filePath) {
		Path filePathInst = Paths.get(filePath);
		if (!filePathInst.isAbsolute()) {
			String msg = "Can not listen modifications on '" + filePath + "'. It is not an absolute filepath.";
			throw new IllegalArgumentException(msg);
		}

		File file = filePathInst.toFile();
		if (!file.exists()) {
			String msg = "Can not listen modifications on '" + filePath + "'. The file does not exists.";
			throw new IllegalArgumentException(msg);
		}
		if (!file.isFile()) {
			String msg = "Can not listen modifications on '" + filePath + "'. It is not a file.";
			throw new IllegalArgumentException(msg);
		}
	}

	private String getDirPath(String filePath) {
		return Paths.get(filePath).getParent().toString();
	}

	private String getFileName(String filePath) {
		List<String> pathElements = toPathElements(Paths.get(filePath));
		return pathElements.get(pathElements.size() - 1);
	}

	private List<String> toPathElements(Path path) {
		return Lists.newArrayList(path.iterator()).stream()
				.map(Path::toString)
				.collect(Collectors.toList());
	}

}
