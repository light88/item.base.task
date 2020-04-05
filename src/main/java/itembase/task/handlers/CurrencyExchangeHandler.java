package itembase.task.handlers;

import itembase.task.configuration.AppConfig;
import itembase.task.dto.ConvertionRequest;
import itembase.task.dto.ConvertionResponse;
import itembase.task.exceptions.ProvidersNotAvailableException;
import itembase.task.provider.CurrencyRateProviderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

//import itembase.task.provider.CurrencyRateObjectProvider;

@Slf4j
@RequiredArgsConstructor
@Component
public class CurrencyExchangeHandler {

	private final AppConfig.CurrencyProviderFactoryObjectProvider currencyRateProviderFactoryObjectFactory;

	public Mono<ServerResponse> handle(ServerRequest request) {
		CurrencyRateProviderFactory currencyRateProviderFactory = currencyRateProviderFactoryObjectFactory.getObject();
		Publisher<?> convertionResponseFlux = request
			.bodyToMono(ConvertionRequest.class)
			.flatMapMany(convertionRequest -> convertionResponseMono(convertionRequest, currencyRateProviderFactory));
//			.switchIfEmpty(Mono.error(new ProvidersNotAvailableException()));

		return ServerResponse.ok().body(convertionResponseFlux, ConvertionResponse.class);
	}

	private Mono<ConvertionResponse> convertionResponseMono(ConvertionRequest convertionRequest, CurrencyRateProviderFactory currencyRateProviderFactory) {
		log.info("@@@@@@@@@@@@@@@@");
		return currencyRateProviderFactory.getCurrencyRateProvider()
			.flatMap(provider ->
				provider.getRates(convertionRequest.getFrom())
					.map(currencyRateData -> new ConvertionResponse(convertionRequest, currencyRateData))
					.onErrorResume(e -> convertionResponseMono(convertionRequest, currencyRateProviderFactory))
			)
			.switchIfEmpty(Mono.error(new ProvidersNotAvailableException()))
			;
	}
}
