package bankingapplication.model.enumType;

import lombok.Getter;

public enum SavingOrCurrentBalance {
    SAVING("Saving", 5000.00), CURRENT("Current", 10000.00);
    private final String value;
    @Getter
    private final double amount;

    SavingOrCurrentBalance(String value, double amount) {
        this.value = value;
        this.amount = amount;
    }
}
