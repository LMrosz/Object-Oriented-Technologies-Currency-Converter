import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConverterTest {

    private CurrencyConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CurrencyConverter(TestRates.sampleRepository());
    }

    @Test
    void convertsPlnToForeignCurrency() {
        assertEquals(new BigDecimal("25.00"), converter.convert(new BigDecimal("100"), "PLN", "USD"));
    }

    @Test
    void convertsForeignCurrencyToPln() {
        assertEquals(new BigDecimal("400.00"), converter.convert(new BigDecimal("100"), "USD", "PLN"));
    }

    @Test
    void convertsBetweenTwoForeignCurrenciesWithRounding() {
        // 100 USD * 4.00 / 4.50 = 88.888... -> 88.89
        assertEquals(new BigDecimal("88.89"), converter.convert(new BigDecimal("100"), "USD", "EUR"));
    }

    @Test
    void takesMultiplierIntoAccount() {
        // kurs JPY podany dla 100 jednostek: 1000 JPY * 0.025 = 25 PLN
        assertEquals(new BigDecimal("25.00"), converter.convert(new BigDecimal("1000"), "JPY", "PLN"));
    }

    @Test
    void sameCurrencyReturnsSameAmount() {
        assertEquals(new BigDecimal("12.34"), converter.convert(new BigDecimal("12.34"), "EUR", "EUR"));
    }

    @Test
    void currencyCodesAreCaseInsensitive() {
        assertEquals(new BigDecimal("25.00"), converter.convert(new BigDecimal("100"), "pln", "usd"));
    }

    @Test
    void resultHasScaleOfTwo() {
        assertEquals(2, converter.convert(new BigDecimal("1"), "PLN", "EUR").scale());
    }

    @Test
    void nullAmountIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert(null, "PLN", "USD"));
    }

    @Test
    void unknownSourceCurrencyIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> converter.convert(BigDecimal.TEN, "XYZ", "USD"));
        assertTrue(e.getMessage().contains("XYZ"));
    }

    @Test
    void unknownTargetCurrencyIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> converter.convert(BigDecimal.TEN, "USD", "XYZ"));
        assertTrue(e.getMessage().contains("XYZ"));
    }
}
