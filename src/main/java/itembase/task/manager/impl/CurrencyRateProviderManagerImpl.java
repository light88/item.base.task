package itembase.task.manager.impl;

import itembase.task.provider.CurrencyRateProvider;
import itembase.task.manager.CurrencyRateProvidersManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.synchronoss.cloud.nio.stream.storage.Disposable;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Component
public class CurrencyRateProviderManagerImpl implements CurrencyRateProvidersManager, Disposable {

	private Queue<CurrencyRateProvider> queue;

	public CurrencyRateProviderManagerImpl(List<CurrencyRateProvider> rateProviders) {
		this.queue = new LinkedList<>(getRandomProviders(rateProviders));
	}

	@Override
	public Mono<CurrencyRateProvider> getNextCurrencyRateProvider() {
		CurrencyRateProvider currencyRateProvider = queue.poll();
		log.info("Return next currency rate provider {}", currencyRateProvider);
		return Mono.justOrEmpty(currencyRateProvider);
	}

	protected List<CurrencyRateProvider> getRandomProviders(List<CurrencyRateProvider> rateProviders) {
		ArrayList<CurrencyRateProvider> currencyRateProviders = new ArrayList<>(rateProviders);
		Collections.shuffle(currencyRateProviders);
		return rateProviders;
	}

	@Override
	public boolean dispose() {
		queue.clear();
		queue = null;
		return true;
	}
}
