package Java;

class Transactions {
    Transaction[] transactions = new Transaction[KootenayKoin.transactionsPerKoin];

    Transactions() {
    }

    Transactions(Transaction[] transactions) {
        this.transactions = transactions;
    }

    public Transaction getTransaction(int i){
        return transactions[i];
    }

    //  Method to populate transaction list with starting index onwards changed only
    public static Transactions generateTransactions(int startIndex){
        Transaction[] transactions = new Transaction[KootenayKoin.transactionsPerKoin];
        for (int i = startIndex; i < KootenayKoin.transactionsPerKoin-1; i++){
            transactions[i] = new Transaction(Math.random() * 10, (int) (Math.random() * 100), (int) (Math.random() * 100));
        }
        // Mining reward
        transactions[KootenayKoin.transactionsPerKoin-1] = new Transaction(100, 1, -1);
        return new Transactions(transactions);
    }

    // Secondary Method to generate full random transaction list with mining reward
    public static Transactions generateTransactions(){
        Transaction[] transactions = new Transaction[KootenayKoin.transactionsPerKoin];
        for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++){
            transactions[i] = new Transaction(Math.random() * 100, (int) (Math.random() * Account.amountOfAccounts),
                    (int) (Math.random() * Account.amountOfAccounts));
        }
        // Mining reward
        transactions[KootenayKoin.transactionsPerKoin-1] = new Transaction(KootenayKoin.miningReward, 1, -1);
        return new Transactions(transactions);
    }

    public static Transactions generateValidTransactions(KootenayKoinBlockchain blockchain){
        Transactions transactions = generateTransactions();

        for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++){
            transactions.setTransaction(i, Transaction.generateValidTransaction(blockchain));
        }

        return transactions;
    }

    public void setTransaction(int i, Transaction transaction){
        this.transactions[i] = transaction;
    }

    public void populateAccountBalances(){
        for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++){
            transactions[i] = new Transaction(10 , (int)(Math.random() * Account.amountOfAccounts) , -1);
        }
    }

    public boolean validate(KootenayKoinBlockchain blockchain) throws InvalidTransactionException{
        for (int i = 0; i < KootenayKoin.transactionsPerKoin-1; i++){
            try {
                this.transactions[i].validate(blockchain);
            } catch(InvalidTransactionException e){
                throw e;
            }
        }

        return true;
    }

    public void addTransaction(Transaction transactionAdded) throws TransactionAdditionException{
        int i = -1;
        for(int j = 1; j < KootenayKoin.transactionsPerKoin;j++){
            if (transactions[j] == null){
                i = j;
                break;
            }
        }
        if (i == -1){
            throw new TransactionAdditionException();
        }
        transactions[i] = transactionAdded;
    }

    // Returns string representation of transaction list
    public String toString() {
        String out = "\n";

        for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++) {
            out = out + transactions[i] + "\n";
        }
        return out + "\n";
    }
}