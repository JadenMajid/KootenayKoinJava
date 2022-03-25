package Java;

public class Transaction {
    public double amount;
    public int addressTo;
    public int addressFrom;

    public Transaction() {
    }

    public Transaction(double amount, int addressTo, int addressFrom) {
        this.amount = amount;
        this.addressTo = addressTo;
        this.addressFrom = addressFrom;
    }

    public String toString() {
        return " " + amount + " " + addressFrom + " " + addressTo;
    }

}