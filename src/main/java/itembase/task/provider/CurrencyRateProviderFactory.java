package itembase.task.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Component
public class CurrencyRateProviderFactory {

	private final Random random = new Random();

	public final List<CurrencyRateProvider> providers;

	public CurrencyRateProvider getRandomProvider() {
		int randomIndex = random.nextInt(providers.size());
		log.info("Next random index : {}", randomIndex);
		return providers.get(randomIndex);
	}

}
