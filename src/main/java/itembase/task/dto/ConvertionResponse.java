package itembase.task.dto;

import itembase.task.model.CurrencyRateData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConvertionResponse {

	public ConvertionResponse(ConvertionRequest cr, CurrencyRateData currencyRateData) {
		this.from = cr.getFrom();
		this.to = cr.getTo();
		this.amount = cr.getAmount();
		this.converted = currencyRateData.getRates().get(cr.getTo()) * cr.getAmount();
	}

	private String from;
	private String to;
	private Double amount;
	private Double converted;
}
