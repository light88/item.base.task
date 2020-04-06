package itembase.task.helper;

import itembase.task.dto.ConvertionBadResponse;
import itembase.task.dto.ConvertionRequest;
import itembase.task.dto.ConvertionResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

public class ConvertionRequester {

	private final WebTestClient webTestClient;

	protected final String CURRENCY_CONVERT = "/currency/convert";

	public ConvertionRequester(WebTestClient webTestClient) {
		this.webTestClient = webTestClient;
	}

	@NotNull
	public FluxExchangeResult<ConvertionResponse> postConvertOK(ConvertionRequest convertionRequest) {
		return webTestClient
			.post()
			.uri(CURRENCY_CONVERT)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(convertionRequest)
			.exchange()
			.expectStatus().isOk()
			.returnResult(ConvertionResponse.class);
	}

	public FluxExchangeResult<ConvertionBadResponse> postConvertBadRequest(ConvertionRequest convertionRequest) {
		return webTestClient
			.post().uri(CURRENCY_CONVERT)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(convertionRequest)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
			.returnResult(ConvertionBadResponse.class);

	}
}
