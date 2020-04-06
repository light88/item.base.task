package itembase.task.handlers;

import itembase.task.attr.ExchangeErrorAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Order(- 2)
@Component
public class ConvertionErrorExceptionHandler extends AbstractErrorWebExceptionHandler {

	public ConvertionErrorExceptionHandler(ServerCodecConfigurer serverCodecConfigurer,
	                                       ExchangeErrorAttributes errorAttributes,
	                                       ApplicationContext applicationContext) {
		super(errorAttributes, new ResourceProperties(), applicationContext);
		super.setMessageWriters(serverCodecConfigurer.getWriters());
		super.setMessageReaders(serverCodecConfigurer.getReaders());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.POST("/currency/convert"), this::handle);
	}

	public Mono<ServerResponse> handle(ServerRequest serverRequest) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(serverRequest, false);
		Object errorResponse = errorAttributes.get("errorResponse");
		if (errorResponse == null) {
			errorResponse = errorAttributes;
		}
		return ServerResponse.badRequest()
			.contentType(MediaType.APPLICATION_PROBLEM_JSON)
			.body(BodyInserters.fromValue(errorResponse));
	}
}
