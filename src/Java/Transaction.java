package Java;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Date;
/**
Transactions are individual transactions that can be added to Transactions
 */
public class Transaction {

    private double amount;
    private int addressTo;
    private int addressFrom;
    private String signature;
    private PublicKey publicKey;
    private long timeValidated;

    public Transaction() {
    }

    // need to refactor to include signature & pubkey in constructors
    public Transaction(double amount, int addressTo, Account sender) {
        this.amount = amount;
        this.addressTo = addressTo;
        addressFrom = sender.getAddress();
        signature = sender.getSignature();
        publicKey = sender.getPublicKey();
        timeValidated = -1; // Set to -1 to indicate that it has not yet been validated
    }

    public static Transaction generateValidTransaction() {
        boolean found = false;
        Transaction transaction = null;

        while (!found) {

            transaction = new Transaction(Math.random(), (int) (Math.random() * Account.amountOfAccounts),
                    Account.miningRewarder);
            try {
                transaction.validate();
                found = true;
            } catch (InvalidTransactionException e) {
                transaction = null;
            }
        }
        return transaction;
    }

    public String generateSignatureTemplate(String senderSignature) {
        return amount + " " + addressTo + " " + addressFrom + " " + senderSignature;
    }

    public String getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public double getAmount() {
        return amount;
    }

    public int getAddressTo() {
        return addressTo;
    }

    public int getAddressFrom() {
        return addressFrom;
    }

    public long getTimeValidated() {
        return timeValidated;
    }

    public String toString() {
        return " " + amount + " " + addressFrom + " " + signature + " " + publicKey + " "
                + addressTo + " " + timeValidated;
    }

    @SuppressWarnings("unused")
    private void updateTimeValidated() {
        Date date = new Date();
        timeValidated = date.getTime();
    }

    // Need to validate signatures in KootenayKoin class
    public boolean validate() throws InvalidTransactionException {
        AsymmetricCryptography decoder = null;
        try {
            decoder = new AsymmetricCryptography();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            return false;
        }

        try { // check if coming from
            if (addressFrom == -1 &&
                    decoder.decryptText(signature, publicKey).substring(0, 2).equals("-1")) {
                return false;
            }
        } catch (InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException
                | BadPaddingException e1) {
            e1.printStackTrace();
            return false;
        }

        // calculates if sender has enough
        if (KootenayKoinBlockchain.calculateBalance(addressFrom) < amount) {
            System.out.println(addressFrom + ": Current balance: "
                    + KootenayKoinBlockchain.calculateBalance(addressFrom) + " -> Send amount: " + amount + "\n");
            throw new InvalidTransactionException(this.toString());
        }

        // calculates if this signature matches the addressFrom and time is right
        try {
            if (!decoder.decryptText(signature, publicKey)
                    .equals(String.valueOf(addressFrom + " " + timeValidated))) {
                throw new InvalidTransactionException(this.toString());
            }
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException
                | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getPureTransaction() {
        return "" + amount + " " + addressTo + " " + addressFrom;
    }
}
