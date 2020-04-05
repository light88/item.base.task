package itembase.task.router;

import itembase.task.handlers.CurrencyExchangeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class CurrencyExchangeRouter {

	@Bean
	RouterFunction<ServerResponse> routes(CurrencyExchangeHandler handler) {
		return RouterFunctions
			.route(POST("/currency/convert"), handler::handle);
	}
}