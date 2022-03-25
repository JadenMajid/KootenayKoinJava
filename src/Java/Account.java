package Java;

public class Account {
    public int address = 0;
    public KootenayKoinBlockchain blockchain = null;

    public Account(){}

    public Account(int address, KootenayKoinBlockchain blockchain){
        this.address = address;
        this.blockchain = blockchain;
    }

    public double calculateBalance(){

        double balance = 0;
        for (KootenayKoin koin: blockchain.blockchain) {
            Transactions transactions = koin.getTransactions();
            for (int i = 0; i<KootenayKoin.transactionsPerKoin; i++) {
                if (transactions.getTransaction(i).addressFrom == this.address) {
                    balance -= transactions.getTransaction(i).amount;
                } else if (transactions.getTransaction(i).addressTo == this.address) {
                    balance += transactions.getTransaction(i).amount;
                }
            }
        }
        return balance;
    }
}
