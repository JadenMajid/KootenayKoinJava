package Java;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.LinkedList;
/**
Accounts are individual accounts that can send or be sent KootenayKoins
 */
public class Account {

    static int amountOfAccounts = 10;
    static KootenayKoinBlockchain blockchain;
    static LinkedList<Account> accounts;
    static Account miningRewarder;

    private int address;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private GenerateKeys keyGenerator;
    private AsymmetricCryptography signatureGen;

    static { // initializes list of accounts could use this to initalize them
        // init static stuff
        accounts = new LinkedList<>();
        miningRewarder = new Account(-1);
        accounts.add(miningRewarder);
        for (int i = 0; i < amountOfAccounts; i++) {
            accounts.add(new Account(i));
        }
    }

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

    public String encryptText(String input) {
        AsymmetricCryptography decoder = null;
        try {
            decoder = new AsymmetricCryptography();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        String temp = "";
        System.out.println(input.length());
        try {
            if (decoder != null) {
                temp = decoder.encryptText(input, this.privateKey);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException
                | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return temp;
    }

    public String toString() {
        return String.valueOf(address);
    }

    public static void setActiveBlockchain(KootenayKoinBlockchain _blockchain) {
        blockchain = _blockchain;
    }

    public int getAddress() {
        return this.address;
    }

    public String getSignature() {
        Date date = new Date();
        try {
            return signatureGen.encryptText(String.valueOf(address) + date.getTime(), privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | UnsupportedEncodingException
                | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Transaction createTransaction(double amount, int addressTo) {
        return new Transaction(amount, addressTo, this);
    }

    public double calculateBalance() {

        double balance = 0;
        
        for (KootenayKoin koin : KootenayKoinBlockchain.getBlockchain()) {
            Transactions transactions = koin.getTransactions();
            for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++) {
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
