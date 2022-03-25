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

    public static Transaction generateValidTransaction(KootenayKoinBlockchain blockchain) {
        boolean found = false;
        Transaction transaction = new Transaction(Math.random(),
                (int) (Math.random() * Account.amountOfAccounts),
                (int) (Math.random() * Account.amountOfAccounts));
        while (! found){
            transaction = new Transaction(Math.random(),
                    (int) (Math.random() * Account.amountOfAccounts),
                    (int) (Math.random() * Account.amountOfAccounts));
            try {
                transaction.validate(blockchain);
                found = true;
            } catch (InvalidTransactionException e) {
                transaction = null;
            }
        }
        return transaction;
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

        if (KootenayKoinBlockchain.calculateBalance(addressFrom, blockchain) < amount){
            System.out.println(addressFrom + ": Current balance: " + KootenayKoinBlockchain.calculateBalance(addressFrom, blockchain) + " -> Send amount: " + amount + "\n");
            throw new InvalidTransactionException(this.toString());
        }
    }
}

