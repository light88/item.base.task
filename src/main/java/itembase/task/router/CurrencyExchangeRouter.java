package itembase.task.router;

import itembase.task.handlers.CurrencyExchangeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CurrencyExchangeRouter {

	@Bean
	RouterFunction<ServerResponse> routes(CurrencyExchangeHandler handler) {
		return route(POST("/currency/convert"), handler::handle);
	}
}