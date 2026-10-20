import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    void constructorStoresValuesAndUppercasesCode() {
        Currency currency = new Currency("dolar amerykański", "usd", new BigDecimal("4.0123"), 1);

        assertEquals("dolar amerykański", currency.getName());
        assertEquals("USD", currency.getCode());
        assertEquals(new BigDecimal("4.0123"), currency.getAverageRate());
        assertEquals(1, currency.getMultiplier());
    }

    @Test
    void ratePerUnitEqualsAverageRateWhenMultiplierIsOne() {
        Currency currency = new Currency("euro", "EUR", new BigDecimal("4.25"), 1);

        assertEquals(0, new BigDecimal("4.25").compareTo(currency.getRatePerUnit()));
    }

    @Test
    void ratePerUnitIsDividedByMultiplier() {
        Currency currency = new Currency("jen (Japonia)", "JPY", new BigDecimal("2.5"), 100);

        assertEquals(0, new BigDecimal("0.025").compareTo(currency.getRatePerUnit()));
    }

    @Test
    void zeroMultiplierIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Currency("euro", "EUR", BigDecimal.ONE, 0));
    }

    @Test
    void negativeMultiplierIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Currency("euro", "EUR", BigDecimal.ONE, -1));
    }

    @Test
    void nullCodeIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Currency("euro", null, BigDecimal.ONE, 1));
    }

    @Test
    void blankCodeIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Currency("euro", "   ", BigDecimal.ONE, 1));
    }

    @Test
    void nullAverageRateIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Currency("euro", "EUR", null, 1));
    }
}
