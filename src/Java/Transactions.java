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
    public static Transactions generateTransactions(int startIndex, Account account){
        Transaction[] transactions = new Transaction[KootenayKoin.transactionsPerKoin];
        for (int i = startIndex; i < KootenayKoin.transactionsPerKoin-1; i++){
            transactions[i] = new Transaction(Math.random() * 10, (int) (Math.random() * 100), account);
        }
        // Mining reward
        transactions[KootenayKoin.transactionsPerKoin-1] = new Transaction(100, 1, account);
        return new Transactions(transactions);
    }

    // Secondary Method to generate full random transaction list, all transactions sent to account argument with mining reward
    public static Transactions generateTransactions(Account account){
        Transaction[] transactions = new Transaction[KootenayKoin.transactionsPerKoin];
        for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++){
            transactions[i] = new Transaction(Math.random() * 100, (int) (Math.random() * Account.amountOfAccounts),
                    account);
        }
        // Mining reward
        transactions[KootenayKoin.transactionsPerKoin-1] = new Transaction(KootenayKoin.miningReward, 1, account);
        return new Transactions(transactions);
    }

    public static Transactions generateValidTransactions(Account account){
        Transactions transactions = new Transactions();

        for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++){
            transactions.setTransaction(i, Transaction.generateValidTransaction(account));
        }

        return transactions;
    }

    public void setTransaction(int i, Transaction transaction){
        this.transactions[i] = transaction;
    }

    public void populateAccountBalances(Account account){
        for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++){
            transactions[i] = new Transaction(10. , (int)(Math.random() * Account.amountOfAccounts) , account);
        }
    }

    public void validate() throws  InvalidTransactionException{
        for (int i = 0; i < KootenayKoin.transactionsPerKoin-1; i++){
            try {
                this.transactions[i].validate();
            } catch(InvalidTransactionException e){
                throw e;
            }
        }

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