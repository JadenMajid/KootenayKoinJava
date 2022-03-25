package Java;

public class Account {
    static int amountOfAccounts = 10;

    private int address;
    public static KootenayKoinBlockchain blockchain;

    public Account(){}

    public Account(int address){
        this.address = address;
    }

    public static void setActiveBlockchain(KootenayKoinBlockchain _blockchain) {
        blockchain = _blockchain;
    }

    public int getAddress() {
        return this.address;
    }

    public Transaction createTransaction(double amount, int addressTo){
        return new Transaction(amount, addressTo, this.address);
    }

    public double calculateBalance(){

        double balance = 0;
        for (KootenayKoin koin: blockchain.blockchain) {
            Transactions transactions = koin.getTransactions();
            for (int i = 0; i< KootenayKoin.transactionsPerKoin; i++) {
                if (transactions.getTransaction(i).getAddressFrom() == this.address) {
                    balance -= transactions.getTransaction(i).getAmount();
                } else if (transactions.getTransaction(i).getAddressTo() == this.address) {
                    balance += transactions.getTransaction(i).getAmount();
                }
            }
        }
        return balance;
    }
}
