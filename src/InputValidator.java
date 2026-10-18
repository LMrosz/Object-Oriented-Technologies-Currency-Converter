import java.math.BigDecimal;

public class InputValidator {
    private RatesRepository repository;

    public InputValidator(RatesRepository repository) {

        this.repository = repository;
    }

    public String validateCode(String code){
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Currency code is required");
        }

        String normalized = code.trim().toUpperCase();

        if (repository.findByCode(normalized) == null) {
            throw new IllegalArgumentException("Unknown currency code: " + normalized);
        }

        return normalized;
    }

    public BigDecimal parseAmount(String numberToParse){
        if (numberToParse == null || numberToParse.isBlank()) {
            throw new IllegalArgumentException("Amount is required");
        }

        String normalized = numberToParse.trim().replace(",", ".");
        BigDecimal amount;

        try {
            amount = new BigDecimal(normalized);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount: " + numberToParse);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        return amount;
    }
}
