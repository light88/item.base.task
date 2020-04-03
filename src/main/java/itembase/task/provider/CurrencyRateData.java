package itembase.task.provider;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class CurrencyRateData {
	private String currency;
	private LocalDate date;
	private LocalDate lastUpdate;
	private Map<String, Double> rates = new HashMap<>();
}
