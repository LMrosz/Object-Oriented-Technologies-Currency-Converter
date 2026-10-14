import java.math.BigDecimal;
import java.math.RoundingMode;

public class Currency {
    private String name;
    private String code;
    private BigDecimal averageRate;
    private int multiplier;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getAverageRate() {
        return averageRate;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public BigDecimal getRatePerUnit(){
        return averageRate.divide(BigDecimal.valueOf(multiplier), 10, RoundingMode.HALF_UP);
    }

    public Currency(String name, String code, BigDecimal averageRate, int multiplier) {

        if (multiplier <= 0) {
            throw new IllegalArgumentException("multiplier must be > 0");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code is required");
        }
        if (averageRate == null) {
            throw new IllegalArgumentException("averageRate is required");
        }

        this.name = name;
        this.code = code.toUpperCase();
        this.averageRate = averageRate;
        this.multiplier = multiplier;
    }
}

