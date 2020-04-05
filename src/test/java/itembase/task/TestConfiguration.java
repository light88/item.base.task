package itembase.task;

import itembase.task.provider.CurrencyRateData;
import itembase.task.provider.CurrencyRateProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class TestConfiguration {

	@Bean
	public CurrencyRateProvider one() {
		return (currency) -> Mono.just(new CurrencyRateData());
	}

	@Bean
	public CurrencyRateProvider two() {
		return (currency) -> Mono.just(new CurrencyRateData());
	}
}
