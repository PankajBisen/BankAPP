package bankingapplication.model.enumType;

public enum Constant {
    INTEREST(0.5), TOTAL(100);
    private final double constantValue;

    Constant(double constantValue) {
        this.constantValue = constantValue;
    }

    public double getConstant() {
        return constantValue;
    }
}
