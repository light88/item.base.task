package itembase.task;

import itembase.task.attr.ExchangeErrorAttributes;
import itembase.task.dto.ConvertionBadResponse;
import itembase.task.dto.ConvertionRequest;
import itembase.task.dto.ConvertionResponse;
import itembase.task.exceptions.ProvidersNotAvailableException;
import itembase.task.handlers.ConvertionErrorExceptionHandler;
import itembase.task.handlers.CurrencyExchangeHandler;
import itembase.task.helper.ConvertionRequester;
import itembase.task.model.CurrencyRateData;
import itembase.task.provider.CurrencyRateProvider;
import itembase.task.manager.CurrencyRateProvidersManager;
import itembase.task.router.CurrencyExchangeRouter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = {TaskApplication.class})
@ContextConfiguration(classes = {
	ExchangeErrorAttributes.class,
	ConvertionErrorExceptionHandler.class,
	CurrencyRateProvidersManager.class,
	CurrencyExchangeRouter.class,
	CurrencyExchangeHandler.class})
@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestCurrencyExchange {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	CurrencyExchangeRouter router;

	@MockBean
	CurrencyRateProvidersManager manager;

	private ConvertionRequester convertionRequester;

	final String eur = "EUR";
	final String usd = "USD";

	@BeforeAll
	public void before() {
		WebTestClient webTestClient = WebTestClient
			.bindToApplicationContext(applicationContext)
			.build();
		this.convertionRequester = new ConvertionRequester(webTestClient);
	}

	@Test
	public void testReturnErrorWhenNoProvidersLeft() {
		when(manager.getNextCurrencyRateProvider()).thenReturn(Mono.empty());

		ConvertionRequest convertionRequest = new ConvertionRequest(usd, eur, 1.0);
		FluxExchangeResult<ConvertionBadResponse> fluxExchangeResult =
			convertionRequester.postConvertBadRequest(convertionRequest);

		ConvertionBadResponse response =
			new ConvertionBadResponse(ProvidersNotAvailableException.class.getSimpleName(),
				ProvidersNotAvailableException.message);

		StepVerifier.create(fluxExchangeResult.getResponseBody())
			.expectNext(response)
			.verifyComplete();
	}

	@Test
	public void testExceptionWhenNextProviderCallReturnsException() {
		when(manager.getNextCurrencyRateProvider()).thenReturn(Mono.error(new IllegalStateException("Oops!")));

		ConvertionRequest convertionRequest = new ConvertionRequest(usd, eur, 10D);

		FluxExchangeResult<ConvertionBadResponse> fluxExchangeResult = convertionRequester.postConvertBadRequest(convertionRequest);

		ConvertionBadResponse response = new ConvertionBadResponse(null, "Oops!");

		StepVerifier.create(fluxExchangeResult.getResponseBody())
			.expectNext(response)
			.verifyComplete();
	}

	@Test
	public void testProviderResult() {
		CurrencyRateData currencyRateData = new CurrencyRateData(usd, LocalDate.now(), LocalDate.now(), Map.of(eur, 1.3));
		CurrencyRateProvider provider = (s) -> Mono.just(currencyRateData);

		when(manager.getNextCurrencyRateProvider())
			.thenReturn(Mono.just(provider))
			.thenReturn(Mono.empty());

		ConvertionRequest convertionRequest = new ConvertionRequest(usd, eur, 10D);

		FluxExchangeResult<ConvertionResponse> fluxExchangeResult = convertionRequester.postConvertOK(convertionRequest);

		ConvertionResponse convertionResponse = new ConvertionResponse(usd, eur, 10D, 13D);

		StepVerifier.create(fluxExchangeResult.getResponseBody())
			.expectNext(convertionResponse)
			.verifyComplete();
	}

	@Test
	public void testErrorWhenErrorProviderNextEmptyProvider() {
		CurrencyRateData currencyRateData = new CurrencyRateData(usd, LocalDate.now(), LocalDate.now(), Map.of(eur, 1.3));

		CurrencyRateProvider okProvider = (p) -> Mono.just(currencyRateData);
		CurrencyRateProvider errorProvider = (p) -> Mono.error(new IllegalStateException("Oops!"));

		when(manager.getNextCurrencyRateProvider())
			.thenReturn(Mono.just(errorProvider))
			.thenReturn(Mono.empty());

		ConvertionRequest convertionRequest = new ConvertionRequest(usd, eur, 10D);

		FluxExchangeResult<ConvertionBadResponse> fluxExchangeResult = convertionRequester.postConvertBadRequest(convertionRequest);

		ConvertionBadResponse response =
			new ConvertionBadResponse(ProvidersNotAvailableException.class.getSimpleName(),
				ProvidersNotAvailableException.message);

		StepVerifier.create(fluxExchangeResult.getResponseBody())
			.expectNext(response)
			.verifyComplete();
	}

}
