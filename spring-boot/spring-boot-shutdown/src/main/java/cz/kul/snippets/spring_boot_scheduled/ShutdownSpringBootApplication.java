package cz.kul.snippets.spring_boot_scheduled;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.Lifecycle;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.Spring;

/*

Spring offers several way how to handle application shutdown:
* DisposableBean.destroy()
* ApplicationListener<ContextClosedEvent>
* SmartLifecycle interface (I haven't tried yet)
* bean destroy method

I do not know if the order of these method is defined, but Spring call them according to following rules:
* first order the beans according to precedence
* call onApplicationEvent() from the highest precedence
* call destroy() and the custom bean destroy method from the lowest precedence

So this app writes out something like this:

Shutdown action: ContextClosedEvent   Bean: prioHighest     Timestamp: 376481466672816 Thread: SpringApplicationShutdownHook
Shutdown action: ContextClosedEvent   Bean: prioZero        Timestamp: 376481467108985 Thread: SpringApplicationShutdownHook
Shutdown action: ContextClosedEvent   Bean: prioLowest      Timestamp: 376481467398485 Thread: SpringApplicationShutdownHook
Shutdown action: destroy              Bean: prioLowest      Timestamp: 376481468527213 Thread: SpringApplicationShutdownHook
Shutdown action: destroyMethod        Bean: prioLowest      Timestamp: 376481468914743 Thread: SpringApplicationShutdownHook
Shutdown action: destroy              Bean: prioZero        Timestamp: 376481469090795 Thread: SpringApplicationShutdownHook
Shutdown action: destroyMethod        Bean: prioZero        Timestamp: 376481469244537 Thread: SpringApplicationShutdownHook
Shutdown action: destroy              Bean: prioHighest     Timestamp: 376481469357353 Thread: SpringApplicationShutdownHook
Shutdown action: destroyMethod        Bean: prioHighest     Timestamp: 376481469467034 Thread: SpringApplicationShutdownHook

 */
@SpringBootApplication
public class ShutdownSpringBootApplication
{

	public static void logShutdownAction(String actionName, String beanName, long nanoTimestamp)
	{
		System.out.printf("Shutdown action: %-20s Bean: %-15s Timestamp: %s Thread: %s%n", actionName, beanName, nanoTimestamp, Thread.currentThread().getName());
	}

	public static class AServiceThatNeedACleanupDuringShutdown implements
		ApplicationListener<ContextClosedEvent>,
		DisposableBean,
		Ordered
	{
		private int order;
		private String beanName;

		public AServiceThatNeedACleanupDuringShutdown(final int order, final String beanName)
		{
			this.order = order;
			this.beanName = beanName;
		}

		@Override
		public void onApplicationEvent(final ContextClosedEvent event)
		{
			logShutdownAction("ContextClosedEvent", beanName, System.nanoTime());

		}

		@Override
		public void destroy() throws Exception
		{
			logShutdownAction("destroy", beanName, System.nanoTime());
		}

		public void destroyMethod()
		{
			logShutdownAction("destroyMethod", beanName, System.nanoTime());
		}

		@Override
		public int getOrder()
		{
			return order;
		}

	}

	@Bean(destroyMethod = "destroyMethod")
	AServiceThatNeedACleanupDuringShutdown prioHighest() {
		return new AServiceThatNeedACleanupDuringShutdown(Ordered.HIGHEST_PRECEDENCE, "prioHighest");
	}

	@Bean(destroyMethod = "destroyMethod")
	AServiceThatNeedACleanupDuringShutdown prioZero() {
		return new AServiceThatNeedACleanupDuringShutdown(0, "prioZero");
	}

	@Bean(destroyMethod = "destroyMethod")
	AServiceThatNeedACleanupDuringShutdown prioLowest() {
		return new AServiceThatNeedACleanupDuringShutdown(Ordered.LOWEST_PRECEDENCE, "prioLowest");
	}


	public static void main(String[] args) {
		SpringApplication.run(ShutdownSpringBootApplication.class, args);
	}

}
