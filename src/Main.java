import java.math.BigDecimal;
import java.util.Scanner;

void main() {
    Fetcher fetcher = new HttpFetcher("https://static.nbp.pl/dane/kursy/xml/LastA.xml");
    RatesProvider provider = new NbpRatesProvider(fetcher, new NbpXmlParser());
    RatesRepository repo = new RatesRepository();
    repo.load(provider);

    InputValidator validator = new InputValidator(repo);
    CurrencyConverter converter = new CurrencyConverter(repo);
    ResultPresenter presenter = new ResultPrinter();

    Scanner sc = new Scanner(System.in);
    System.out.println("Currency converter (NBP). Type 'exit' to quit.");

    while (true) {
        try {
            System.out.print("From currency (or exit): ");
            String fromInput = sc.nextLine();
            if (fromInput.trim().equalsIgnoreCase("exit")) {
                break;
            }
            String fromCode = validator.validateCode(fromInput);

            System.out.print("Amount: ");
            String amountInput = sc.nextLine();
            if (amountInput.trim().equalsIgnoreCase("exit")) {
                break;
            }
            BigDecimal amount = validator.parseAmount(amountInput);

            System.out.print("To currency: ");
            String toInput = sc.nextLine();
            if (toInput.trim().equalsIgnoreCase("exit")) {
                break;
            }
            String toCode = validator.validateCode(toInput);

            BigDecimal result = converter.convert(amount, fromCode, toCode);
            presenter.showResult(result, fromCode, toCode);
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println();
        }
    }

    System.out.println("Bye.");
    sc.close();
}