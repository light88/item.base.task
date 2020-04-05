package itembase.task.attr;

import itembase.task.exceptions.ProvidersNotAvailableException;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {

		if ((getError(request) instanceof ProvidersNotAvailableException)) {
			ProvidersNotAvailableException exception = (ProvidersNotAvailableException) getError(request);

			Map<String, Object> errorAttributes = new LinkedHashMap<>();

			errorAttributes.put("exception", exception.getClass().getSimpleName());
			errorAttributes.put("message", exception.getMessage());
//			errorAttributes.put("status", exception.getStatus().value());
//			errorAttributes.put("error", exception.getStatus().getReasonPhrase());

			return errorAttributes;
		}

		return super.getErrorAttributes(request, includeStackTrace);
	}
}
