import java.math.BigDecimal;
import java.util.List;

/**
 * Wspólne dane testowe: repozytorium z kilkoma walutami bez łączenia się z NBP.
 */
final class TestRates {

    private TestRates() {
    }

    static List<Currency> sampleCurrencies() {
        return List.of(
                new Currency("dolar amerykański", "USD", new BigDecimal("4.0000"), 1),
                new Currency("euro", "EUR", new BigDecimal("4.5000"), 1),
                new Currency("jen (Japonia)", "JPY", new BigDecimal("2.5000"), 100)
        );
    }

    static RatesRepository sampleRepository() {
        RatesRepository repository = new RatesRepository();
        repository.load(TestRates::sampleCurrencies);
        return repository;
    }
}
