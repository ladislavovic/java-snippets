package cz.kul.snippets;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class SnippetsTest {

    @Rule
    public SnippetsTestWatcher watcher = new SnippetsTestWatcher();

    private MemmoryAppenderHelper memmoryAppenderHelper;
    private FilesystemHelper filesystemHelper;

    @Before
    public void snippetsTestBefore() {
        memmoryAppenderHelper = MemmoryAppenderHelper.getInstance();
        filesystemHelper = FilesystemHelper.getInstance();
    }

    protected MemmoryAppenderHelper getMemmoryAppenderHelper() {
        return memmoryAppenderHelper;
    }

    protected FilesystemHelper getFilesystemHelper() {
        return filesystemHelper;
    }

    protected MemmoryAppender getMemmoryAppender() {
        return getMemmoryAppenderHelper().getAppender();
    }

    protected final void assertMessageInLog(String message) {
        org.junit.Assert.assertTrue(getMemmoryAppender().findEventsCount(message) > 0);
    }

    protected final void assertMessageCountInLog(int times, String message) {
        Assert.assertEquals(times, getMemmoryAppender().findEventsCount(message));
    }

    protected void testOut(String pattern, Object... args) {
        System.out.printf(watcher.getTestId() + ": " + pattern + "\n", args);
    }

    private static class SnippetsTestWatcher extends TestWatcher {

        private String testId;

        @Override
        protected void starting(Description description) {
            testId = description.getTestClass().getName() + "." + description.getMethodName();
        }

        public String getTestId() {
            return testId;
        }
    }

}
