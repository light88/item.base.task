package itembase.task.provider;

import itembase.task.model.CurrencyRateData;
import reactor.core.publisher.Mono;

public interface CurrencyRateProvider {
	Mono<CurrencyRateData> getRates(String currency);
}
