package Java;

/**
Transactions are a container class for an array of Transaction. They hold Transaction and methods.
 */
class Transactions {

    private Transaction[] transactions;

    Transactions() {
        transactions = new Transaction[KootenayKoin.transactionsPerKoin];
    }

    Transactions(Transaction[] transactions) {
        this.transactions = transactions;
    }

    public Transaction getTransaction(int i) {
        return transactions[i];
    }


    public Transactions generateValidTransactions() {
        for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++) {
            transactions[i] = Transaction.generateValidTransaction();
        }

        return this;
    }

    public String getPureTransactions() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++) {
            if (transactions[i]!= null) {
                output.append(transactions[i].getPureTransaction()).append("\n");
            }
        }
        return output.toString();
    }

    public void setTransaction(int i, Transaction transaction) {
        transactions[i] = transaction;
    }

    public void populateAccountBalances() {
        for (int i = 0; i < KootenayKoin.transactionsPerKoin - 1; i++) {
            transactions[i] = new Transaction(KootenayKoin.miningReward, (int) (Math.random() * Account.amountOfAccounts),
                    Account.miningRewarder);
        }
    }

    public void validate() throws InvalidTransactionException {
        for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++) {
            try {
                if (transactions[i] != null) {
                    transactions[i].validate();
                }
            } catch (InvalidTransactionException e) {
                throw e;
            }
        }

    }

    public void addTransaction(Transaction transactionAdded) throws TransactionAdditionException {
        int i = -1;
        for (int j = 1; j < KootenayKoin.transactionsPerKoin; j++) {
            if (transactions[j] == null) {
                i = j;
                break;
            }
        }
        if (i == -1) {
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

    public void setLastTransaction(Transaction transaction) {
        transactions[KootenayKoin.transactionsPerKoin - 1] = transaction;
    }
}