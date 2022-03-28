package Java;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Date;

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
        this.addressFrom = sender.getAddress();
        this.signature = sender.getSignature();
        this.publicKey = sender.getPublicKey();
        this.timeValidated = -1; // Set to -1 to indicate that it has not yet been validated
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
        return this.amount + " " + this.addressTo + " " + this.addressFrom + " " + senderSignature;
    }

    public String getSignature() {
        return this.signature;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
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

    public long getTimeValidated() {
        return this.timeValidated;
    }

    public String toString() {
        return " " + this.amount + " " + this.addressFrom + " " + this.signature + " " + this.publicKey + " "
                + this.addressTo + " " + this.timeValidated;
    }

    private void updateTimeValidated() {
        Date date = new Date();
        this.timeValidated = date.getTime();
    }

    // Need to validate signatures in KootenayKoin class
    public void validate() throws InvalidTransactionException {
        AsymmetricCryptography decoder = null;
        try {
            decoder = new AsymmetricCryptography();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try { // check if coming from
            if (this.addressFrom == -1 &&
                    decoder.decryptText(this.signature, this.publicKey).substring(0, 2).equals("-1")) {
                return;
            }
        } catch (InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException
                | BadPaddingException e1) {
            e1.printStackTrace();
        }

        // calculates if sender has enough
        if (KootenayKoinBlockchain.calculateBalance(addressFrom) < amount) {
            System.out.println(addressFrom + ": Current balance: "
                    + KootenayKoinBlockchain.calculateBalance(addressFrom) + " -> Send amount: " + amount + "\n");
            throw new InvalidTransactionException(this.toString());
        }

        // calculates if this signature matches the addressFrom and time is right
        try {
            if (!decoder.decryptText(this.signature, this.publicKey)
                    .equals(String.valueOf(this.addressFrom + " " + timeValidated))) {
                throw new InvalidTransactionException(this.toString());
            }
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException
                | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getPureTransaction() {
        return "" + amount + " " + addressTo + " " + addressFrom;
    }
}
