package Java;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Miner {
    public int address;
    public int DIFFICULTY = 1;
    public KootenayKoin coin = null;
    public KootenayKoinBlockchain blockchain = new KootenayKoinBlockchain();

    public Miner(){
    }

    public Miner(int address){
        this.address = address;
    }

    public Miner(int address, KootenayKoinBlockchain blockchain){
        this.address = address;
        this.blockchain = blockchain;
    }

    public KootenayKoin getBlock(int i){
        return blockchain.getBlock(i);
    }

    public void makeKootenayKoin(){
        coin = new KootenayKoin();
    }

    public KootenayKoin mine(KootenayKoin noNonceKoin){
        boolean foundNonce = false;
        int nonce = 0;
        String value = "";
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }


        while (!foundNonce && nonce < Integer.MAX_VALUE){
            nonce++;
            noNonceKoin.setNonce(nonce);
            value = noNonceKoin.toString();
            digest.reset();

            try {
                digest.update(value.getBytes("utf8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            value = String.format("%040x", new BigInteger(1, digest.digest()));
            System.out.println(value);

            if (value.substring(0,DIFFICULTY) == "0".repeat(DIFFICULTY)){
                System.out.println("\n==========MINTED COIN==========" + noNonceKoin + "\n===============================");
                blockchain.addKoinToChain(noNonceKoin);
                foundNonce = true;
                return noNonceKoin;
            }
        }

        noNonceKoin.setNonce(0);
        return noNonceKoin;
    }

    public void createGenesisBlock(String genesisString, Transactions transactions, int difficulty) {
        blockchain.addKoinToChain(KootenayKoin.createGenesisBlock(genesisString, transactions, difficulty));
    }
}
