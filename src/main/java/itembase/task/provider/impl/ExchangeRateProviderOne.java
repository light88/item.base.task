package itembase.task.provider.impl;

import itembase.task.model.CurrencyRateData;
import itembase.task.provider.CurrencyRateProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * https://api.exchangerate-api.com/v4/latest/EUR
 * <p>
 * response ex.
 * <p>
 * base: "EUR",
 * date: "2020-04-02",
 * time_last_updated: 1585785849,
 * rates: {
 * EUR: 1,
 * AED: 4.027712,
 * ...
 * }
 */
@Slf4j
@RequiredArgsConstructor
@Component
class ExchangeRateProviderOne implements CurrencyRateProvider {

	public static final String BASE_URL = "https://api.exchangerate-api.com/v4/latest/{currency}";
	private final WebClient webClient;

	@Override
	public Mono<CurrencyRateData> getRates(String currency) {
		log.info(getClass().getSimpleName());
		return webClient
			.get()
			.uri(BASE_URL, currency)
			.retrieve()
			.bodyToMono(ResponseProviderOne.class)
			.log()
			.map(ResponseProviderOneAdapter::new);
	}

	@Data
	@NoArgsConstructor
	static class ResponseProviderOne {
		String base;
		LocalDate date;
		long time_last_updated;
		Map<String, Double> rates = new HashMap<>();
	}

	static class ResponseProviderOneAdapter extends CurrencyRateData {

		private final ResponseProviderOne response;

		ResponseProviderOneAdapter(ResponseProviderOne response) {
			this.response = response;
		}

		@Override
		public LocalDate getLastUpdate() {
			return LocalDate.ofEpochDay(response.time_last_updated);
		}

		@Override
		public String getCurrency() {
			return response.getBase();
		}

		@Override
		public LocalDate getDate() {
			return LocalDate.from(response.getDate());
		}

		@Override
		public Map<String, Double> getRates() {
			return Collections.unmodifiableMap(response.getRates());
		}
	}
}
