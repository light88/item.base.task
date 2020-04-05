package itembase.task.configuration;

import itembase.task.provider.CurrencyRateProvider;
import itembase.task.provider.manager.CurrencyRateProvidersManager;
import itembase.task.provider.manager.impl.CurrencyRateProviderManagerImpl;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class AppConfig {

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}

	@Bean
	public ObjectFactory<CurrencyRateProvidersManager> currencyRateProviderFactory(List<CurrencyRateProvider> providers) {
		return () -> new CurrencyRateProviderManagerImpl(providers);
	}

}
