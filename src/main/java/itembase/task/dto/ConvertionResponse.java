package itembase.task.dto;

import itembase.task.provider.CurrencyRateData;
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
