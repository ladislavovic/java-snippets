package cz.kul.snippets.concurrency._05_monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Basic way how to synchronize concurrent threads in Java are "monitors".
 * 
 * All "synchronized" blocks belongs into the monitor (synchronized method is 
 * only shortcut for synchronized (this) { ... )
 * 
 * Only one thread at time can hold the monitor.
 * 
 * @author kulhalad
 * @since 7.4
 *
 */
public class MainMonitor {

    public static void main(String[] args) throws Exception {
        String monitor1 = "monitor1";
        String monitor2 = "monitor2";
        System.out.println("monitor1 sys hash: " + System.identityHashCode(monitor1));
        System.out.println("monitor2 sys hash: " + System.identityHashCode(monitor2));
        
        logMonitors(); // no monitor yet

        synchronized (monitor1) {
            logMonitors(); // one monitor
            synchronized (monitor2) {
                logMonitors(); // two monitors
            }
            logMonitors(); // one monitor again
        }

        logMonitors(); // no monitor again
    }
    
    public static void logMonitors() {
        long currThreadId = Thread.currentThread().getId();
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = mxBean.getThreadInfo(new long [] { currThreadId }, true, false);
        MonitorInfo[] monitorInfos = threadInfos[0].getLockedMonitors();
        System.out.println("Monitors (" + monitorInfos.length + "):");
        for (MonitorInfo mi : monitorInfos ) {
            System.out.println("class: " + mi.getClassName() + " hash: " + mi.getIdentityHashCode());
        }
        System.out.println();
    }

}
