package cz.kul.snippets;

import org.junit.Assert;
import org.junit.Before;

public class SnippetsTest {

    private MemmoryAppenderHelper memmoryAppenderHelper;
    private FilesystemHelper filesystemHelper;

    @Before
    public void before() {
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

}
