package itembase.task.exceptions;

import org.springframework.core.NestedRuntimeException;

public class ProvidersNotAvailableException extends NestedRuntimeException {

	public static final String message = "No providers available to serve your request";

	public ProvidersNotAvailableException() {
		super(message);
	}

}
