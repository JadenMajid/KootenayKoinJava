package Java;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


class KootenayKoin {
    static int transactionsPerKoin = 8;
    static double miningReward = 10;

    int blockNumber = 0;
    int difficulty = 1;
    private String previousHash;
    private int nonce;
    private Transactions transactions;
    static int transactionsPerKoin = 8;


    public KootenayKoin(){}

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

    public static KootenayKoin createGenesisKoin(String genesisMessage, Transactions transactions, int difficulty){
        return new KootenayKoin(genesisMessage, 0, transactions, 0, difficulty);
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    // sets nonce
    public void setNonce(int nonce){
        this.nonce = nonce;
    }

    public Transactions getTransactions() {
        return this.transactions;
    }

    // Returns the data of the block
    public String toString(){
        return blockNumber + "," + difficulty + "," + previousHash + "," + nonce + "," + transactions;
    }

    // Hashes this coin and returns String value
    public String hash(){
        String value = this.toString();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        digest.reset();
        try {
            digest.update(value.getBytes("utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        value = String.format("%040x", new BigInteger(1, digest.digest()));

        return value;
    }

    // Validation method to validate this block as valid, can also be accessed with a String for prev hash
    public boolean validate() throws InvalidKootenayKoinException, InvalidTransactionException {
        String value = this.hash();
        if (!value.substring(0, this.difficulty).equals("0".repeat(this.difficulty))){
            System.out.println("Invalid Block:\n" + this.toString());
            throw new InvalidKootenayKoinException("Invalid Block",
                    new KootenayKoin(this.previousHash, this.nonce, this.transactions, this.blockNumber, this.difficulty));
        }

        return true;
    }

    public boolean validate(String previousHash, KootenayKoinBlockchain blockchain) throws InvalidKootenayKoinException {
        String value = this.hash();
        if (value.substring(0, this.difficulty).equals("0".repeat( this.difficulty)) &&
                this.previousHash == previousHash){
            throw new InvalidKootenayKoinException("Invalid Block", new KootenayKoin(this.previousHash, this.nonce, this.transactions, this.blockNumber, this.difficulty));
        }

        return true;
    }

    public boolean validate(String previousHash) throws InvalidKootenayKoinException{
        String value = this.hash();
        if (value.substring(0, this.difficulty).equals("0".repeat( this.difficulty)) &&
                this.previousHash == previousHash){
            throw new InvalidKootenayKoinException("Invalid Block", new KootenayKoin(this.previousHash, this.nonce, this.transactions, this.blockNumber, this.difficulty));
        }
        return true;
    }
}

