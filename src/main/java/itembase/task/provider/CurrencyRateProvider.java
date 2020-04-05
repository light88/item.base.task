package itembase.task.provider;

import reactor.core.publisher.Mono;

//@FunctionalInterface
public interface CurrencyRateProvider {
	Mono<CurrencyRateData> getRates(String currency);
}
