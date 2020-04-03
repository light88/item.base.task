package itembase.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Random;

@SpringBootApplication
public class TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);


//		Random random = new Random(System.currentTimeMillis());
//		Flux.<Integer>generate(synchronousSink -> synchronousSink.next(random.nextInt()))
//			.doOnNext(i -> System.out.println("doOnNext " + i))
//			.doOnSubscribe(subscription -> System.out.println("doOnSubscribe " + subscription.toString()))
//			.doon


	}

}
