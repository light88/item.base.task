package itembase.task.attr;

import itembase.task.dto.ConvertionBadResponse;
import itembase.task.exceptions.ProvidersNotAvailableException;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

/**
 * Custom attributes to process exceptions
 */
@Component
public class ExchangeErrorAttributes extends DefaultErrorAttributes {
	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {

		if ((getError(request) instanceof ProvidersNotAvailableException)) {
			ProvidersNotAvailableException ex = (ProvidersNotAvailableException) getError(request);

			Map<String, Object> ea = super.getErrorAttributes(request, includeStackTrace);
			ConvertionBadResponse errorResponse = new ConvertionBadResponse(ex.getClass().getSimpleName(), ex.getMessage());
			ea.put("errorResponse", errorResponse);
			return ea;
		}

		return super.getErrorAttributes(request, includeStackTrace);
	}
}
