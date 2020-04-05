//package itembase.task.handlers;
//
//import itembase.task.dto.ConvertionRequest;
//import itembase.task.dto.ConvertionResponse;
//import itembase.task.exceptions.ProvidersNotAvailableException;
//import itembase.task.provider.CurrencyRateProviderFactory;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.reactivestreams.Publisher;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class CurrencyExchangeHandlerOLD {
//
//	private final CurrencyRateProviderFactory currencyRateProviderFactory;
//
//	public Mono<ServerResponse> handle(ServerRequest request) {
//		Publisher<?> convertionResponseFlux = request
//			.bodyToMono(ConvertionRequest.class)
//			.flatMapMany(cr ->
//				Flux.fromIterable(currencyRateProviderFactory.getRandomProviders())
//					.flatMap(provider ->
//						provider.getRates(cr.getFrom())
//							.map(currencyRateData ->
//								new ConvertionResponse(cr, currencyRateData))
//
//					))
//			.log()
//			.switchOnFirst((signal, flux) -> signal.hasValue() ? Flux.just(signal.get()) : flux)
////			.subscribeOn(Schedulers.immediate())
//			.onErrorMap(e -> new ProvidersNotAvailableException(HttpStatus.BAD_REQUEST, "No providers available to serve your request"))
////						.onErrorReturn()
//			.onErrorContinue((t, o) -> System.out.println("Error happened " + t.getMessage()))
////						.switchIfEmpty(Mono.just(new ConvertionResponse("FALLBACK", "222", 1.0, 2.0)))
//			;
//
//		return ServerResponse.ok().body(convertionResponseFlux, ConvertionResponse.class);
//	}
//}
