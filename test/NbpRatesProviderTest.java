import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NbpRatesProviderTest {

    @Test
    void passesFetchedDataToParserAndReturnsItsResult() {
        List<Currency> expected = TestRates.sampleCurrencies();
        String[] receivedData = new String[1];

        Fetcher fetcher = () -> "<xml/>";
        RatesParser parser = data -> {
            receivedData[0] = data;
            return expected;
        };

        List<Currency> result = new NbpRatesProvider(fetcher, parser).getRates();

        assertEquals("<xml/>", receivedData[0]);
        assertSame(expected, result);
    }

    @Test
    void worksWithRealXmlParser() {
        Fetcher fetcher = () -> """
                <tabela_kursow>
                  <pozycja>
                    <nazwa_waluty>euro</nazwa_waluty>
                    <przelicznik>1</przelicznik>
                    <kod_waluty>EUR</kod_waluty>
                    <kurs_sredni>4,2500</kurs_sredni>
                  </pozycja>
                </tabela_kursow>
                """;

        List<Currency> result = new NbpRatesProvider(fetcher, new NbpXmlParser()).getRates();

        assertEquals(1, result.size());
        assertEquals("EUR", result.get(0).getCode());
    }

    @Test
    void propagatesFetcherException() {
        Fetcher failingFetcher = () -> {
            throw new RuntimeException("brak połączenia");
        };

        NbpRatesProvider provider = new NbpRatesProvider(failingFetcher, new NbpXmlParser());

        assertThrows(RuntimeException.class, provider::getRates);
    }
}
