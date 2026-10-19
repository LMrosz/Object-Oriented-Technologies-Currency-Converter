import java.math.BigDecimal;

public class ResultPrinter implements ResultPresenter {
    @Override
    public void showResult(BigDecimal amount, String fromCode, String toCode) {
        System.out.printf("Converted amount: %.2f %s%n", amount, toCode);
    }
}
