package itembase.task.handlers;

import itembase.task.dto.ConvertionRequest;
import itembase.task.dto.ConvertionResponse;
import itembase.task.exceptions.ProvidersNotAvailableException;
import itembase.task.manager.CurrencyRateProvidersManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Currency exchange handler that process a request a pass it through providers
 * to get currency rates back and returns {@see ConvertionResponse} conversion response as a result.
 * If no providers return successful result respond with error
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CurrencyExchangeHandler {

	private final ObjectFactory<CurrencyRateProvidersManager> objectFactory;

	public Mono<ServerResponse> handle(ServerRequest request) {
		CurrencyRateProvidersManager manager = objectFactory.getObject();
		Publisher<?> convertionResponseFlux = request
			.bodyToMono(ConvertionRequest.class)
			.log()
			.flatMapMany(convertionRequest -> convertionResponseMono(convertionRequest, manager))
			.switchIfEmpty(Mono.error(new ProvidersNotAvailableException()));

		return ServerResponse.ok().body(convertionResponseFlux, ConvertionResponse.class);
	}

	private Mono<ConvertionResponse> convertionResponseMono(ConvertionRequest convertionRequest, CurrencyRateProvidersManager manager) {
		log.info("convertionResponseMono method call");
		return manager.getNextCurrencyRateProvider()
			.log()
			.flatMap(provider ->
				provider.getRates(convertionRequest.getFrom())
					.map(currencyRateData -> new ConvertionResponse(convertionRequest, currencyRateData))
					.onErrorResume(e -> convertionResponseMono(convertionRequest, manager))
			)
			.switchIfEmpty(Mono.error(new ProvidersNotAvailableException()))
			;
	}
}
