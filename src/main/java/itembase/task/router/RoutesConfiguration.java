package itembase.task.router;

import itembase.task.dto.ConvertionRequest;
import itembase.task.dto.ConvertionResponse;
import itembase.task.provider.CurrencyRateProvider;
import itembase.task.provider.CurrencyRateProviderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class RoutesConfiguration {

	@Bean
	WebClient webClient() {
		return WebClient.builder().build();
	}

	@Bean
	RouterFunction<ServerResponse> routes(CurrencyRateProviderFactory currencyRateProviderFactory) {
		return RouterFunctions
			.route(POST("/currency/convert"), request -> {
				Mono<ConvertionResponse> convertionResponseMono =
					request
						.bodyToMono(ConvertionRequest.class)
						.flatMap(cr ->
							currencyRateProviderFactory.getRandomProvider().getRates(cr.getFrom())
								.map(crd -> {
									Double rate = crd.getRates().get(cr.getTo());
									return new ConvertionResponse(cr.getFrom(), cr.getTo(), cr.getAmount(), rate * cr.getAmount());
								})
								.onErrorContinue((e, o) -> {
									System.out.println("------------------");
									System.out.println(e.getMessage());
									System.out.println(o.getClass());
									System.out.println(o);
									System.out.println("------------------");
								})
						);
				return ServerResponse.ok().body(convertionResponseMono, ConvertionResponse.class);
			})
			.andRoute(
				POST("/currency/convert1"), request -> {
					Mono<ConvertionRequest> convertionRequestMono =
						request
							.bodyToMono(ConvertionRequest.class);


					Mono<ConvertionResponse> map =
						convertionRequestMono
							.log()
							.flatMap(cr -> {
//							Flux.fromStream(currencyRateProviderFactory.providers.stream()).map(p ->
								return currencyRateProviderFactory.providers.get(0).getRates(cr.getFrom())
									.log()
									.map(crd -> {
										Double rate = crd.getRates().get(cr.getTo());
										return new ConvertionResponse(cr.getFrom(), cr.getTo(), cr.getAmount(), rate * cr.getAmount());
									});

								//.blockFirst()
							});
					return ServerResponse.ok().body(map, String.class);
				})
			;


	}
}