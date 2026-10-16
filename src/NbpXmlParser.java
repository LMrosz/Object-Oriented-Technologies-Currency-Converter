import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NbpXmlParser implements RatesParser {
    @Override
    public List<Currency> parse(String data) {
        List<Currency> currencies = new ArrayList<>();
        if (data == null || data.isBlank()) {
            return currencies;
        }
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(data)));
            NodeList pozycje = doc.getElementsByTagName("pozycja");
            for (int i = 0; i < pozycje.getLength(); i++) {
                Element e = (Element) pozycje.item(i);
                String name = text(e, "nazwa_waluty");
                String code = text(e, "kod_waluty");
                String rateStr = text(e, "kurs_sredni");
                String multiplierStr = text(e, "przelicznik");
                if (code.isBlank() || rateStr.isBlank() || multiplierStr.isBlank()) {
                    continue;
                }
                BigDecimal rate = new BigDecimal(rateStr.replace(",", "."));
                int multiplier = Integer.parseInt(multiplierStr);
                currencies.add(new Currency(name, code, rate, multiplier));
            }
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas parsowania XML z NBP", e);
        }
        return currencies;
    }
    private String text(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() == 0) {
            return "";
        }
        return nodes.item(0).getTextContent().trim();
    }
}
