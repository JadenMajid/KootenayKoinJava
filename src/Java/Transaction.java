package Java;

public class Transaction {
    private double amount;
    private int addressTo;
    private int addressFrom;

    public Transaction() {
    }

    public Transaction(double amount, int addressTo, int addressFrom) {
        this.amount = amount;
        this.addressTo = addressTo;
        this.addressFrom = addressFrom;
    }

    public double getAmount() {
        return this.amount;
    }

    public int getAddressTo() {
        return this.addressTo;
    }

    public int getAddressFrom() {
        return this.addressFrom;
    }

    public String toString() {
        return " " + amount + " " + addressFrom + " " + addressTo;
    }

}