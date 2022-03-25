package Java;

public class Account {
    public int address = 0;
    public KootenayKoinBlockchain blockchain = new KootenayKoinBlockchain();

    public Account(){}

    public Account(int address, KootenayKoinBlockchain blockchain){
        this.address = address;
        this.blockchain = blockchain;
    }

    public Account(int address){
        this.address = address;
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

    public static double calculateBalance(int address, KootenayKoinBlockchain blockchain){

        double balance = 0;

        for (KootenayKoin koin: blockchain.blockchain) {
            Transactions transactions = koin.getTransactions();
            for (int i = 0; i< KootenayKoin.transactionsPerKoin; i++) {
                if (transactions.getTransaction(i).getAddressFrom() == address) {
                    balance -= transactions.getTransaction(i).getAmount();
                } else if (transactions.getTransaction(i).getAddressTo() == address) {
                    balance += transactions.getTransaction(i).getAmount();
                }
            }
        }
        return balance;
    }
}
