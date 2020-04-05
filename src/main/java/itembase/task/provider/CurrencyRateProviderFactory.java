package itembase.task.provider;

import reactor.core.publisher.Mono;

public interface CurrencyRateProviderFactory {

	Mono<CurrencyRateProvider> getCurrencyRateProvider();
}
