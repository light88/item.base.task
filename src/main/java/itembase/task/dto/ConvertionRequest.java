package itembase.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConvertionRequest {
	private String from;
	private String to;
	private Double amount;
}
