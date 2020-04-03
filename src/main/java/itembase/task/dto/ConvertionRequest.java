package itembase.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
public class ConvertionRequest {
	@Size(min = 3, max = 3)
	private String from;
	@Size(min = 3, max = 3)
	private String to;
	@NotNull
	private Double amount;
}
