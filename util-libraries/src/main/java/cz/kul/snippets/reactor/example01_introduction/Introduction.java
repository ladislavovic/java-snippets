package cz.kul.snippets.reactor.example01_introduction;

import cz.kul.snippets.SnippetsTest;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Introduction extends SnippetsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(Introduction.class);

	@Test
	public void test() {
		List<Integer> elements = new ArrayList<>();

		Flux.just(1, 2, 3)
				.log()
				.subscribe(elements::add);
	}

	@Test
	public void test2() {
		List<Integer> elements = new ArrayList<>();
		Flux.just(1, 2, 3)
				.log()
				.subscribe(new Subscriber<Integer>() {
					private Subscription s;
					int onNextAmount;

					@Override
					public void onSubscribe(Subscription s) {
						this.s = s;
						s.request(2);
					}

					@Override
					public void onNext(Integer integer) {
						elements.add(integer);
						onNextAmount++;
						if (onNextAmount % 2 == 0) {
							s.request(2);
						}
					}

					@Override
					public void onError(Throwable t) {

					}

					@Override
					public void onComplete() {

					}
				});
	}

	@Test
	public void test3() {
		ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
				while(true) {
					fluxSink.next(System.currentTimeMillis());
				}
			})
			.sample(Duration.ofSeconds(2))
			.publish();
		publish.subscribe(x -> LOGGER.info(x.toString()));
		publish.subscribe(x -> LOGGER.info(x.toString()));
		publish.connect();
	}

	@Test
	public void test4() {
		Flux<Integer> stream = Flux
				.just(1, 2, 3)
				.log();
		stream.subscribe(
				x -> System.out.println(x),
				error -> System.out.println("Error: " + error),
				() -> System.out.println("done"),
				sub -> sub.request(2));

	}

	@Test
  public void flatMap() {
		Mono
				.just(1)
				.log()
				.flatMap(x -> {
					return Mono.just(x).filter(y -> {
						if (true) throw new RuntimeException("inner flatmap");
						return true;
					});
				})
				.map(x -> {
					if (x > 0) {
						throw new RuntimeException();
					} else {
						return x;
					}
				})
				.subscribe(
						x -> System.out.println("consumer: " + x),
						err -> System.out.println(err.getMessage()));
  }

	@Test
	public void errorHandling() {
		Mono
				.just(1)
				.map(x -> {
					if (x > 0) {
						throw new RuntimeException("Can not map!");
					} else {
						return Integer.toString(x);
					}
				})
				.map(x -> x + x)
				.subscribe(System.out::println);
	}

	@Test
	public void errorHandlingWithFlatmap() {
		Mono
				.just(1)
				.flatMap(x -> Mono
				    .just(x)
						.map(y -> {
							if (y > 0) {
								throw new RuntimeException("An exception from the inner stream.");
							}
							return Integer.toString(y);
						}))
				.map(x -> x + x)
				.subscribe(
						x -> System.out.println(x),
						err -> err.printStackTrace());
	}

	@Test
	public void nullHanling() {

		// It creates an empty stream
		Mono.empty().subscribe();

		// It creates empty stream
		Mono.justOrEmpty(null).subscribe();

		// Can not create mono with just(null), it throws an exception
		assertThrows(NullPointerException.class, () -> {
			Mono
					.just(null)
					.subscribe();
		});

		// Can not map to null, it throws an exception
		assertThrows(Exception.class, () -> {
			Mono
					.just(1)
					.map(x -> null)
					.subscribe();
		});

		// Example how to handle null values
//		assertThrows(RuntimeException.class, () -> {
			Mono
					.just(1)
					.flatMap(x -> {
						String data = null; // data get from DB for example
						return Mono.justOrEmpty(data);
					})
					.switchIfEmpty(Mono.error(new RuntimeException("My exception")))
					.subscribe();

//		});

	}






}
