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

    public void validate(KootenayKoinBlockchain blockchain) throws InvalidTransactionException{
        if (addressFrom == -1){
            return; // this is just a temporary fix to allow for accounts to have balance added without validation
        }
        System.out.println(KootenayKoinBlockchain.calculateBalance(addressFrom, blockchain) + " -> " + amount + "\n");
        if (KootenayKoinBlockchain.calculateBalance(addressFrom, blockchain) < amount){
            throw new InvalidTransactionException(this.toString());
        }
    }
}