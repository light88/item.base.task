package itembase.task.provider;

import reactor.core.publisher.Mono;

public interface CurrencyRateProvider {
	Mono<CurrencyRateData> getRates(String currency);
}
