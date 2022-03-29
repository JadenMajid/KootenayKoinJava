package Java;

import java.util.LinkedList;
/**
The KootenayKoinBlockchain is the blockchain that all mined KootenayKoins are added to
 */
public class KootenayKoinBlockchain {

    static LinkedList<KootenayKoin> blockchain;

    public KootenayKoinBlockchain(){
        blockchain = new LinkedList<KootenayKoin>();
    }
    public void addKootenayKoin(KootenayKoin koin){
        blockchain.add(koin);
    }

    // Adds Koin to current blockchain
    public void addKoinToChain(KootenayKoin koin){
        try {
            if (koin.validate() || koin.getBlockNumber() != 0){
                blockchain.addLast(koin);
                System.out.println(
                        "==========MINTED COIN==========\n" +
                                koin.getPureKoin());
            }
        } catch(InvalidKootenayKoinException e) {
            System.out.println("Invalid Kootenay Koin detected: \n" + koin);
        } catch(InvalidTransactionException e){
            System.out.println("Invalid Transaction detected: \n" + koin.getTransactions());
        }
    }

    public KootenayKoin getBlock(int i){
        return blockchain.get(i);
    }

    public static double calculateBalance(int address){

        double balance = 0;

        for (KootenayKoin koin: blockchain) {
            Transactions transactions = koin.getTransactions();
            for (int i = 0; i< KootenayKoin.transactionsPerKoin; i++) {
                if (transactions.getTransaction(i).getAddressFrom() == address) {
                    balance -= transactions.getTransaction(i).getAmount();
                } else if (transactions.getTransaction(i).getAddressTo() == address) {
                    balance += transactions.getTransaction(i).getAmount();
                }
            }
        }
        return balance;
    }

    // Validates blockchain
    public boolean validate() throws InvalidKootenayKoinException {
        // checks genesis block
        try {
            blockchain.get(0).validate();
        } catch(InvalidTransactionException e){
            System.out.println("Invalid Transaction detected: \n" + e);
        }
        //
        for (int i = 1; i < blockchain.size(); i++){
            try {
                blockchain.get(i).validate(blockchain.get(i-1).getPreviousHash());
            } catch(InvalidKootenayKoinException e) {
                System.out.println("invalid blockchain");
                return false;
            } catch (InvalidTransactionException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static LinkedList<KootenayKoin> getBlockchain() {
        return blockchain;
    }
    public static int getSize(){return blockchain.size();}
}


