import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RatesRepositoryTest {

    @Test
    void loadStoresAllCurrenciesFromProvider() {
        RatesRepository repository = TestRates.sampleRepository();

        assertNotNull(repository.findByCode("USD"));
        assertNotNull(repository.findByCode("EUR"));
        assertNotNull(repository.findByCode("JPY"));
    }

    @Test
    void loadAddsPlnWhenProviderDoesNotReturnIt() {
        RatesRepository repository = TestRates.sampleRepository();

        Currency pln = repository.findByCode("PLN");
        assertNotNull(pln);
        assertEquals(0, BigDecimal.ONE.compareTo(pln.getAverageRate()));
        assertEquals(1, pln.getMultiplier());
    }

    @Test
    void loadDoesNotOverridePlnReturnedByProvider() {
        Currency customPln = new Currency("złoty", "PLN", new BigDecimal("1.0000"), 1);
        RatesRepository repository = new RatesRepository();

        repository.load(() -> List.of(customPln));

        assertSame(customPln, repository.findByCode("PLN"));
    }

    @Test
    void findByCodeIsCaseInsensitive() {
        RatesRepository repository = TestRates.sampleRepository();

        assertSame(repository.findByCode("USD"), repository.findByCode("usd"));
    }

    @Test
    void findByCodeReturnsNullForUnknownCode() {
        RatesRepository repository = TestRates.sampleRepository();

        assertNull(repository.findByCode("XYZ"));
    }

    @Test
    void getAllReturnsLoadedCurrenciesAndPln() {
        RatesRepository repository = TestRates.sampleRepository();

        List<Currency> all = repository.getAll();

        assertEquals(4, all.size());
        assertTrue(all.stream().anyMatch(c -> c.getCode().equals("PLN")));
    }

    @Test
    void getAllReturnsCopy() {
        RatesRepository repository = TestRates.sampleRepository();

        repository.getAll().clear();

        assertEquals(4, repository.getAll().size());
    }

    @Test
    void emptyProviderLeavesOnlyPln() {
        RatesRepository repository = new RatesRepository();

        repository.load(List::of);

        assertEquals(1, repository.getAll().size());
        assertNotNull(repository.findByCode("PLN"));
    }
}
