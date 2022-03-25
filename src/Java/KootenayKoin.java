package Java;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


class KootenayKoin {
    int blockNumber = 0;
    int difficulty = 1;
    public String previousHash;
    public int nonce = 0;
    public Transactions transactions;

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

    public static KootenayKoin createGenesisBlock(String genesisMessage, Transactions transactions, int difficulty){
        return new KootenayKoin(genesisMessage, 0, transactions, 0, difficulty);
    }

    // sets nonce
    public void setNonce(int nonce){
        this.nonce = nonce;
    }

    // Returns the data of the block
    public String toString(){
        return blockNumber + "," + difficulty + "," + previousHash + "," + nonce + "," + transactions;
    }

    // Hashes this coin and returns String value
    public String hash(){
        String value = "";
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
    public boolean validate() throws InvalidKootenayKoinException {
        String value = this.hash();
        if (value.substring(0, this.difficulty).equals("0".repeat( this.difficulty))){
            throw new InvalidKootenayKoinException("Invalid Block", new KootenayKoin(this.previousHash, this.nonce, this.transactions, this.blockNumber, this.difficulty));
        }

        return true;
    }

    public boolean validate(String previousHash) throws InvalidKootenayKoinException {
        String value = this.hash();
        if (value.substring(0, this.difficulty).equals("0".repeat( this.difficulty)) &&
                this.previousHash == previousHash){
            throw new InvalidKootenayKoinException("Invalid Block", new KootenayKoin(this.previousHash, this.nonce, this.transactions, this.blockNumber, this.difficulty));
        }

        return true;
    }
}

