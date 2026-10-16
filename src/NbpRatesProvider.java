import java.util.List;

public class NbpRatesProvider implements RatesProvider {
    private Fetcher fetcher;
    private RatesParser parser;

    public NbpRatesProvider(Fetcher fetcher, RatesParser parser) {
        this.fetcher = fetcher;
        this.parser = parser;
    }

    @Override
    public List<Currency> getRates() {
        String data = fetcher.fetch();
        return parser.parse(data);
    }
}
