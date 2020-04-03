package itembase.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
public class ConvertionResponse {
	@Size(min = 3, max = 3)
	private String from;
	@Size(min = 3, max = 3)
	private String to;
	@Positive
	private Double amount;
	@Positive
	private Double converted;
}
