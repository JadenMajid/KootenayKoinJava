package Java;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Account {
    static int amountOfAccounts = 10;

    private int address;
    private PrivateKey privateKey;
    private  PublicKey publicKey;
    private GenerateKeys keyGenerator;
    private AsymmetricCryptography signatureGen;
    public static KootenayKoinBlockchain blockchain;

    public Account() {

    }

    public Account(int address) {
        this.address = address;

        try {
            this.keyGenerator = new GenerateKeys(4096);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }

        assert keyGenerator != null;
        keyGenerator.createKeys();

        this.privateKey = keyGenerator.getPrivateKey();
        this.publicKey = keyGenerator.getPublicKey();

        try {
            this.signatureGen = new AsymmetricCryptography();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public static void setActiveBlockchain(KootenayKoinBlockchain _blockchain) {
        blockchain = _blockchain;
    }

    public int getAddress() {
        return this.address;
    }

    public String getSignature(){
        try {
            return signatureGen.encryptText(String.valueOf(address), privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return "XXXXXXXXXX";
    }

    public Transaction createTransaction(double amount, int addressTo){
        return new Transaction(amount, addressTo, this);
    }

    public double calculateBalance(){

        double balance = 0;
        for (KootenayKoin koin : KootenayKoinBlockchain.blockchain) {
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

    public PublicKey getPublicKey() {
        return this.publicKey;
    }
}
