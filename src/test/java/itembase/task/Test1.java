package itembase.task;

import itembase.task.provider.CurrencyRateProvider;
import itembase.task.provider.CurrencyRateProviderFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {
//	CurrencyRateProviderFactory.class,
//	CurrencyExchangeRouter.class,
//	CurrencyExchangeHandler.class})
//@WebFluxTest
public class Test1 {

	//	private static MockWebServer mockWebServer;
//	@BeforeAll
//	public static void beforeAll() throws IOException {
//		mockWebServer = new MockWebServer();
//		mockWebServer.start();
//	}
	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	CurrencyRateProviderFactory currencyRateProviderFactory;

//	@MockBean
//	CurrencyRateProvider provider1;

	@MockBean
	CurrencyRateProvider provider2;

	private WebTestClient webTestClient;

	@Before
	public void before() {
		webTestClient = WebTestClient
			.bindToApplicationContext(applicationContext)
			.build();
	}

//	@Test
//	public void testStart() {
//		Assertions.assertEquals(2, currencyRateProviderFactory.getRandomProviders().size());
//	}

	@Test
	public void test1() {

	}

	@Test
	@Ignore
	public void check() throws InterruptedException {
//		mockWebServer.enqueue(new MockResponse()
//			.setBody("ABC")
//			.addHeader("Content-Type", "application/json"));

//		WebClient webClient = WebClient.create("http://localhost:" + mockWebServer.getPort());

//		Mono<String> stringMono = webClient.get().retrieve().bodyToMono(String.class);
//		StepVerifier.create(stringMono)
//			.expectNextMatches(s -> s.equals("ABC"))
//			.verifyComplete();
//
//		RecordedRequest recordedRequest = mockWebServer.takeRequest();
//
//		assertEquals("GET", recordedRequest.getMethod());
//		assertEquals("/", recordedRequest.getPath());

	}

	@AfterAll
	public static void afterAll() throws IOException {
//		mockWebServer.shutdown();
	}
}
