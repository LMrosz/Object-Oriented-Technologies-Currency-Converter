import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ResultPrinterTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private Locale originalLocale;

    @BeforeEach
    void redirectOutput() {
        originalOut = System.out;
        originalLocale = Locale.getDefault();
        // printf formatuje liczby wg domyślnego Locale (w Polsce przecinek), ustawiamy stałe
        Locale.setDefault(Locale.US);
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
        Locale.setDefault(originalLocale);
    }

    @Test
    void printsAmountWithTwoDecimalPlacesAndTargetCode() {
        new ResultPrinter().showResult(new BigDecimal("25"), "PLN", "USD");

        assertEquals("Converted amount: 25.00 USD" + System.lineSeparator(),
                output.toString(StandardCharsets.UTF_8));
    }

    @Test
    void roundsAmountToTwoDecimalPlaces() {
        new ResultPrinter().showResult(new BigDecimal("88.889"), "USD", "EUR");

        assertTrue(output.toString(StandardCharsets.UTF_8).contains("88.89 EUR"));
    }
}
