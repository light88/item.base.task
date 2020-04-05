package itembase.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurrencyRateData {
	private String currency;
	private LocalDate date;
	private LocalDate lastUpdate;
	private Map<String, Double> rates = new HashMap<>();
}
