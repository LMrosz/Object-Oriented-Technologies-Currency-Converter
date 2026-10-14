import java.util.List;

public interface RatesParser {
    public List<Currency> parse(String data);
}
