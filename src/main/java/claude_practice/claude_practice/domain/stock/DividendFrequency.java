package claude_practice.claude_practice.domain.stock;

public enum DividendFrequency {
    MONTHLY(12),
    QUARTERLY(4),
    SEMI_ANNUAL(2),
    ANNUAL(1);

    private final int paymentsPerYear;

    DividendFrequency(int paymentsPerYear) {
        this.paymentsPerYear = paymentsPerYear;
    }

    public int paymentsPerYear() {
        return paymentsPerYear;
    }
}
