package itembase.task.configuration;

import itembase.task.provider.CurrencyRateProvider;
import itembase.task.provider.CurrencyRateProviderFactory;
import itembase.task.provider.CurrencyRateProviderFactoryImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class AppConfig {
	@Bean
	WebClient webClient() {
		return WebClient.builder().build();
	}

	@Bean
	public CurrencyProviderFactoryObjectProvider currencyRateProviderFactory(List<CurrencyRateProvider> providers) {
		return new CurrencyProviderFactoryObjectProvider(providers);
	}

	public class CurrencyProviderFactoryObjectProvider implements ObjectFactory<CurrencyRateProviderFactory> {
		private List<CurrencyRateProvider> providers;

		public CurrencyProviderFactoryObjectProvider(List<CurrencyRateProvider> providers) {
			this.providers = providers;
		}

		@Override
		public CurrencyRateProviderFactory getObject() throws BeansException {
			return new CurrencyRateProviderFactoryImpl(providers);
		}
	}
}
