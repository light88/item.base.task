package itembase.task;

import itembase.task.attr.CustomErrorAttributes;
import itembase.task.dto.ConvertionRequest;
import itembase.task.dto.ConvertionResponse;
import itembase.task.exceptions.ProvidersNotAvailableException;
import itembase.task.handlers.CurrencyExchangeHandler;
import itembase.task.handlers.GlobalErrorExceptionHandler;
import itembase.task.model.CurrencyRateData;
import itembase.task.provider.CurrencyRateProvider;
import itembase.task.provider.manager.CurrencyRateProvidersManager;
import itembase.task.router.CurrencyExchangeRouter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = {TaskApplication.class})
@ContextConfiguration(classes = {
	CustomErrorAttributes.class,
	GlobalErrorExceptionHandler.class,
	CurrencyRateProvidersManager.class,
	CurrencyExchangeRouter.class,
	CurrencyExchangeHandler.class})
@WebFluxTest
public class TestCurrencyExchange {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	CurrencyExchangeRouter router;

	@MockBean
	CurrencyRateProvidersManager manager;

	private WebTestClient webTestClient;

	final String eur = "EUR";
	final String usd = "USD";

	@BeforeEach
	public void before() {
		webTestClient = WebTestClient
			.bindToApplicationContext(applicationContext)
			.build();
	}

	@Test
	void checkWebClient() {
		assertNotNull(webTestClient, "WebTestClient is NULL");
	}

	@Test
	public void testEmptyProvidersException() {
		Mockito.when(manager.getNextCurrencyRateProvider()).thenReturn(Mono.empty());

		webTestClient
			.post().uri("/currency/convert")
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(new ConvertionRequest("", "", 1.0))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.exception").isEqualTo(ProvidersNotAvailableException.class.getSimpleName())
			.jsonPath("$.message").isEqualTo(ProvidersNotAvailableException.message)
		;
	}
//	{
//	  "exception" -> "ProvidersNotAvailableException",
//	  "message" -> "No providers available to serve your request"
//  }


	@Test
	public void testEmptyProviderAndOneProviderException() {
		CurrencyRateProvider provider = (s) ->
			Mono.just(new CurrencyRateData("USD", LocalDate.now(), LocalDate.now(), Collections.emptyMap()));
		Mockito.when(manager.getNextCurrencyRateProvider()).thenReturn(Mono.just(provider));
		Mockito.when(manager.getNextCurrencyRateProvider()).thenReturn(Mono.empty());

		webTestClient
			.post().uri("/currency/convert")
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(new ConvertionRequest("", "", 1.0))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.exception").isEqualTo(ProvidersNotAvailableException.class.getSimpleName())
			.jsonPath("$.message").isEqualTo(ProvidersNotAvailableException.message)
		;
	}

	@Test
	public void testProviderResult() {
		Map<String, Double> rates = new HashMap<>();
		rates.put(eur, 1.3);

		CurrencyRateProvider provider = (s) ->
			Mono.just(new CurrencyRateData(usd, LocalDate.now(), LocalDate.now(), rates));
		Mockito.when(manager.getNextCurrencyRateProvider()).thenReturn(Mono.empty());
		Mockito.when(manager.getNextCurrencyRateProvider()).thenReturn(Mono.just(provider));

		ConvertionRequest convertionRequest = new ConvertionRequest(usd, eur, 10D);

		Flux<ConvertionResponse> responseFlux = webTestClient
			.post().uri("/currency/convert")
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(convertionRequest)
			.exchange()
			.expectStatus().isOk()
			.returnResult(ConvertionResponse.class)
			.getResponseBody();

		ConvertionResponse convertionResponse = responseFlux.blockFirst();


		Assertions.assertEquals(13D, convertionResponse.getConverted());
		Assertions.assertEquals(usd, convertionRequest.getFrom());
		Assertions.assertEquals(eur, convertionRequest.getTo());
		Assertions.assertEquals(10D, convertionRequest.getAmount());

		;
	}

}
