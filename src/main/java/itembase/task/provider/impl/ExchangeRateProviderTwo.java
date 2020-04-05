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
import java.util.HashMap;
import java.util.Map;

/**
 * https://api.exchangeratesapi.io/latest?base=EUR
 * <p>
 * response ex.
 */

@Slf4j
@RequiredArgsConstructor
@Component
class ExchangeRateProviderTwo implements CurrencyRateProvider {

	private final WebClient webClient;

	@Override
	public Mono<CurrencyRateData> getRates(String currency) {
//		log.info("ERROR client 2");
//		return Mono.error(new RuntimeException("2222222"));
		log.info("GET RATES client 2");
		return webClient
			.get()
			.uri("https://api.exchangeratesapi.io/latest?base={currency}", currency)
			.retrieve()
			.bodyToMono(Response.class)
			.log()
			.map(Adapter::new);
	}

	//	rates: {
//		CAD: 1.5451,
//			HKD: 8.4545,
//			ISK: 155.3,
//	base: "EUR",
//	date: "2020-04-02"

	@NoArgsConstructor
	@Data
	public static class Response {
		String base;
		LocalDate date;
		Map<String, Double> rates = new HashMap<>();
	}

	public static class Adapter extends CurrencyRateData {

		private final Response response;

		public Adapter(Response response) {
			this.response = response;
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
