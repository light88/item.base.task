package itembase.task.validator;

import itembase.task.dto.ConvertionRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ExchangeRequestValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return ConvertionRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

	}
}
