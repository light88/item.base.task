package itembase.task.provider.manager;

import itembase.task.provider.CurrencyRateProvider;
import reactor.core.publisher.Mono;

public interface CurrencyRateProvidersManager {
	Mono<CurrencyRateProvider> getNextCurrencyRateProvider();
}
