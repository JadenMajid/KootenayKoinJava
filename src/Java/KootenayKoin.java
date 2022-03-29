package Java;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
KootenayKoins are individual blocks that can be added to the KootenayKoinBlockchain
 */
class KootenayKoin {

    static int transactionsPerKoin = 8;
    static double miningReward = 10;

    int blockNumber = 0;
    int difficulty = 1;
    private String previousHash;
    private int nonce;
    private Transactions transactions;

    public KootenayKoin() {
    }

    // Full constructor to initalize all values
    public KootenayKoin(String previousHash, int nonce, Transactions transactions, int blockNumber, int difficulty) {
        this.previousHash = previousHash;
        this.nonce = nonce;
        this.transactions = transactions;
        this.blockNumber = blockNumber;
        this.difficulty = difficulty;
    }

    public KootenayKoin(String previousHash, Transactions transactions, int blockNumber, int difficulty) {
        this.previousHash = previousHash;
        this.nonce = 0;
        this.transactions = transactions;
        this.blockNumber = blockNumber;
        this.difficulty = difficulty;
    }

    public static KootenayKoin createGenesisKoin(String genesisMessage, Transactions transactions) {
        return new KootenayKoin(genesisMessage, 0, transactions, 0, 0);
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    // sets nonce
    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public Transactions getTransactions() {
        return this.transactions;
    }

    // Returns the data of the block
    public String toString() {
        return blockNumber + "," + difficulty + "," + previousHash + "," + nonce + "," + transactions;
    }

    // Hashes this coin and returns String value
    public String hash() {
        String value = this.toString();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (digest != null) {
            digest.reset();
            digest.update(value.getBytes(StandardCharsets.UTF_8));
            value = String.format("%040x", new BigInteger(1, digest.digest()));
        }
        return value;
    }

    // Validation method to validate this block as valid, can also be accessed with
    // a String for prev hash
    public boolean validate() throws InvalidKootenayKoinException {

        String value = this.hash();
        if (!value.substring(0, this.difficulty).equals("0".repeat(this.difficulty))) {
            System.out.println("Invalid Block:\n" + this);
            throw new InvalidKootenayKoinException("Invalid Block",
                    new KootenayKoin(this.previousHash, this.nonce, this.transactions, this.blockNumber,
                            this.difficulty));
        }
        
        try {
            transactions.validate();
        } catch (InvalidTransactionException e) {
            System.err.println("Invalid transaction detected.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void setLastTransaction(Transaction transaction) {
        this.transactions.setLastTransaction(transaction);
    }

    public void validate(String previousHash) throws InvalidKootenayKoinException, InvalidTransactionException {
        String value = this.hash();
        if (value.substring(0, this.difficulty).equals("0".repeat(this.difficulty)) &&
                this.previousHash.equals(previousHash)) {
            throw new InvalidKootenayKoinException("Invalid Block", new KootenayKoin(this.previousHash, this.nonce,
                    this.transactions, this.blockNumber, this.difficulty));
        }
        transactions.validate();
    }

    public String getPureKoin() {
        String output = "";
        output = "Hash: " + hash() + "\nNonce: " + nonce + "\nBlock #: " + blockNumber + "\nPrevious Hash: " + previousHash + "\nTransactions:\n"  + transactions.getPureTransactions();
        return output;
    }
}
