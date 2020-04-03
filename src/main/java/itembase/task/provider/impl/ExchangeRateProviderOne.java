package itembase.task.provider.impl;

import itembase.task.provider.CurrencyRateData;
import itembase.task.provider.CurrencyRateProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * https://api.exchangerate-api.com/v4/latest/EUR
 */
@RequiredArgsConstructor
@Component
class ExchangeRateProviderOne implements CurrencyRateProvider {

	private final WebClient webClient;

	@Override
	public Mono<CurrencyRateData> getRates(String currency) {
		return webClient
			.get()
			.uri("https://api.exchangerate-api.com/v4/latest/{currency}", currency)
			.retrieve()
			.bodyToMono(Response.class)
			.log()
			.map(Adapter::new);
	}

//	base: "EUR",
//	date: "2020-04-02",
//	time_last_updated: 1585785849,
//	rates: {
//		EUR: 1,
//		AED: 4.027712,
//	}

	@Data
	@NoArgsConstructor
	public static class Response {
		String base;
		LocalDate date;
		long time_last_updated;
		Map<String, Double> rates = new HashMap<>();
	}

	public static class Adapter extends CurrencyRateData {

		private final Response response;

		public Adapter(Response response) {
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
			return response.getDate();
		}

		@Override
		public Map<String, Double> getRates() {
			return response.getRates();
		}
	}
}
