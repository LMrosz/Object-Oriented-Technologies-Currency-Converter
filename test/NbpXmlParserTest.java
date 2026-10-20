import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NbpXmlParserTest {

    private static final String SAMPLE_XML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <tabela_kursow typ="A">
              <numer_tabeli>203/A/NBP/2026</numer_tabeli>
              <data_publikacji>2026-10-20</data_publikacji>
              <pozycja>
                <nazwa_waluty>dolar amerykański</nazwa_waluty>
                <przelicznik>1</przelicznik>
                <kod_waluty>USD</kod_waluty>
                <kurs_sredni>4,0123</kurs_sredni>
              </pozycja>
              <pozycja>
                <nazwa_waluty>jen (Japonia)</nazwa_waluty>
                <przelicznik>100</przelicznik>
                <kod_waluty>JPY</kod_waluty>
                <kurs_sredni>2,6789</kurs_sredni>
              </pozycja>
              <pozycja>
                <nazwa_waluty>niekompletna</nazwa_waluty>
                <przelicznik>1</przelicznik>
                <kod_waluty>XXX</kod_waluty>
              </pozycja>
            </tabela_kursow>
            """.strip();

    private NbpXmlParser parser;

    @BeforeEach
    void setUp() {
        parser = new NbpXmlParser();
    }

    @Test
    void parsesAllCompletePositions() {
        List<Currency> currencies = parser.parse(SAMPLE_XML);

        assertEquals(2, currencies.size());
    }

    @Test
    void parsesNameCodeRateAndMultiplier() {
        Currency usd = parser.parse(SAMPLE_XML).get(0);

        assertEquals("dolar amerykański", usd.getName());
        assertEquals("USD", usd.getCode());
        assertEquals(new BigDecimal("4.0123"), usd.getAverageRate());
        assertEquals(1, usd.getMultiplier());
    }

    @Test
    void parsesMultiplierOtherThanOne() {
        Currency jpy = parser.parse(SAMPLE_XML).get(1);

        assertEquals("JPY", jpy.getCode());
        assertEquals(100, jpy.getMultiplier());
    }

    @Test
    void skipsPositionWithMissingRate() {
        List<Currency> currencies = parser.parse(SAMPLE_XML);

        assertTrue(currencies.stream().noneMatch(c -> c.getCode().equals("XXX")));
    }

    @Test
    void nullInputReturnsEmptyList() {
        assertTrue(parser.parse(null).isEmpty());
    }

    @Test
    void blankInputReturnsEmptyList() {
        assertTrue(parser.parse("   ").isEmpty());
    }

    @Test
    void xmlWithoutPositionsReturnsEmptyList() {
        assertTrue(parser.parse("<tabela_kursow></tabela_kursow>").isEmpty());
    }

    @Test
    void malformedXmlThrowsRuntimeException() {
        assertThrows(RuntimeException.class, () -> parser.parse("<tabela_kursow><pozycja>"));
    }
}
