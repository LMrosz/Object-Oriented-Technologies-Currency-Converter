import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyConverter {
    private RatesRepository repository;

    public CurrencyConverter(RatesRepository repository) {
        this.repository = repository;
    }

    public BigDecimal convert(BigDecimal amount, String fromCode, String toCode) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }

        Currency fromCurrency = repository.findByCode(fromCode);
        Currency toCurrency = repository.findByCode(toCode);

        if (fromCurrency == null) {
            throw new IllegalArgumentException("Invalid currency code " + fromCode);
        }

        if (toCurrency == null) {
            throw new IllegalArgumentException("Invalid currency code " + toCode);
        }

        BigDecimal fromRate = fromCurrency.getRatePerUnit();
        BigDecimal toRate = toCurrency.getRatePerUnit();

        return amount.multiply(fromRate).divide(toRate, 2, RoundingMode.HALF_UP);
    }
}
