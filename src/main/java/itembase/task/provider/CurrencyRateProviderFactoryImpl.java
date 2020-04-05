package itembase.task.provider;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
//@RequiredArgsConstructor
//@Component
//@Scope(
//	value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
//	proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrencyRateProviderFactoryImpl implements CurrencyRateProviderFactory {

	private final Queue<CurrencyRateProvider> queue;

	public CurrencyRateProviderFactoryImpl(List<CurrencyRateProvider> rateProviders) {
		this.queue = new LinkedList<>(getRandomProviders(rateProviders));
	}

	@Override
	public Mono<CurrencyRateProvider> getCurrencyRateProvider() {
//		Optional<CurrencyRateProvider> providerOptional = Optional.ofNullable(queue.poll());
//		providerOptional.ifPresentOrElse(p -> { }, () -> queue.addAll(providers));
//		return Mono.justOrEmpty(providerOptional);
		return Mono.justOrEmpty(queue.poll());
	}

	public List<CurrencyRateProvider> getRandomProviders(List<CurrencyRateProvider> rateProviders) {
		ArrayList<CurrencyRateProvider> currencyRateProviders = new ArrayList<>(rateProviders);
		Collections.shuffle(currencyRateProviders);
		return currencyRateProviders;
	}
}
