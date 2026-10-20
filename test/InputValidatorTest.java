import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    private InputValidator validator;

    @BeforeEach
    void setUp() {
        validator = new InputValidator(TestRates.sampleRepository());
    }

    @Test
    void validateCodeReturnsKnownCode() {
        assertEquals("USD", validator.validateCode("USD"));
    }

    @Test
    void validateCodeTrimsAndUppercases() {
        assertEquals("EUR", validator.validateCode("  eur "));
    }

    @Test
    void validateCodeAcceptsPln() {
        assertEquals("PLN", validator.validateCode("pln"));
    }

    @Test
    void validateCodeRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> validator.validateCode(null));
    }

    @Test
    void validateCodeRejectsBlank() {
        assertThrows(IllegalArgumentException.class, () -> validator.validateCode("   "));
    }

    @Test
    void validateCodeRejectsUnknownCode() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> validator.validateCode("abc"));
        assertTrue(e.getMessage().contains("ABC"));
    }

    @Test
    void parseAmountAcceptsDot() {
        assertEquals(new BigDecimal("12.50"), validator.parseAmount("12.50"));
    }

    @Test
    void parseAmountAcceptsComma() {
        assertEquals(new BigDecimal("12.50"), validator.parseAmount("12,50"));
    }

    @Test
    void parseAmountTrimsWhitespace() {
        assertEquals(new BigDecimal("100"), validator.parseAmount("  100  "));
    }

    @Test
    void parseAmountRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> validator.parseAmount(null));
    }

    @Test
    void parseAmountRejectsBlank() {
        assertThrows(IllegalArgumentException.class, () -> validator.parseAmount(""));
    }

    @Test
    void parseAmountRejectsText() {
        assertThrows(IllegalArgumentException.class, () -> validator.parseAmount("abc"));
    }

    @Test
    void parseAmountRejectsZero() {
        assertThrows(IllegalArgumentException.class, () -> validator.parseAmount("0"));
    }

    @Test
    void parseAmountRejectsNegative() {
        assertThrows(IllegalArgumentException.class, () -> validator.parseAmount("-5"));
    }
}
