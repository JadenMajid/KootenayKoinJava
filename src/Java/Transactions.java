package Java;

class Transactions {
    Transaction[] transactions;

    Transactions() {
    }

    Transactions(Transaction[] transactions) {
        this.transactions = transactions;
    }

    //  Method to populate transaction list with starting index changed only
    public static Transactions generateTransactions(int startIndex){
        Transaction[] transactions = new Transaction[128];
        for (int i = startIndex; i < 128; i++){
            transactions[i] = new Transaction(Math.random(), (int) (Math.random() * 100), (int) (Math.random() * 100));
        }
        return new Transactions(transactions);
    }

    // Secondary Method to generate full transaction list
    public static Transactions generateTransactions(){
        Transaction[] transactions = new Transaction[128];
        for (int i = 0; i < 128; i++){
            transactions[i] = new Transaction(Math.random(), (int) (Math.random() * 100), (int) (Math.random() * 100));
        }
        return new Transactions(transactions);
    }

    // Returns string representation of transaction list
    public String toString() {
        String out = "\n";

        for (int i = 0; i < 127; i++) {
            out = out + transactions[i] + "\n";
        }
        return out + "\n";
    }
}