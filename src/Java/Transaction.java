package Java;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class Transaction {
    private double amount;
    private int addressTo;
    private int addressFrom;
    private String signature;
    private PublicKey publicKey;


    public Transaction() {
    }

    // need to refactor to include signature & pubkey in constructors
    public Transaction(double amount, int addressTo, Account sender) {
        this.amount = amount;
        this.addressTo = addressTo;
        this.addressFrom = sender.getAddress();
        this.signature = sender.getSignature();
        this.publicKey = sender.getPublicKey();
    }

    public static Transaction generateValidTransaction(Account account) {
        boolean found = false;
        Transaction transaction = null;;

        while (! found){

            transaction = new Transaction(Math.random(), (int) (Math.random() * Account.amountOfAccounts), account);
            try {
                transaction.validate();
                found = true;
            } catch (InvalidTransactionException e) {
                transaction = null;
            }
        }
        return transaction;
    }

    public String getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public double getAmount() {
        return this.amount;
    }

    public int getAddressTo() {
        return this.addressTo;
    }

    public int getAddressFrom() {
        return this.addressFrom;
    }


    public String toString() {
        return " " + amount + " " + addressFrom + " " + signature + " " + publicKey + " " + addressTo;
    }

    // Need to validate signatures in KootenayKoin class
    public void validate() throws InvalidTransactionException{
        AsymmetricCryptography decoder = null;
        try {
            decoder = new AsymmetricCryptography();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        if (KootenayKoinBlockchain.calculateBalance(addressFrom) < amount){
            System.out.println(addressFrom + ": Current balance: " + KootenayKoinBlockchain.calculateBalance(addressFrom) + " -> Send amount: " + amount + "\n");
            throw new InvalidTransactionException(this.toString());
        }

        try {
            if (decoder.decryptText(this.signature ,this.publicKey).equals(String.valueOf(addressFrom))){
                return; // this is just a temporary fix to allow for accounts to have balance added without validation
            }           // although -1 would be a convenient option for mining reward
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        throw new InvalidTransactionException(this.toString());
    }
}

