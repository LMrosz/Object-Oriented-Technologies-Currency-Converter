import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class RatesRepository {
    private Map<String, Currency> rates;

    public RatesRepository() {
        this.rates = new HashMap<>();
    }

    public void load(RatesProvider provider){
        List<Currency> currencies = provider.getRates();
        for (Currency currency : currencies) {
            rates.put(currency.getCode(), currency);
        }

        if (!rates.containsKey("PLN")) {
            rates.put("PLN", new Currency("polski złoty", "PLN", BigDecimal.ONE, 1));
        }
    }


    public Currency findByCode(String code) {
        return rates.get(code.toUpperCase());
    }

    public List<Currency> getAll() {
        return new ArrayList<Currency>(rates.values());
    }
}
