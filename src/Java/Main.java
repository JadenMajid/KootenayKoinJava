package Java;

import static Java.KootenayKoinBlockchain.blockchain;
import static Java.KootenayKoinBlockchain.calculateBalance;

public class Main{
    public static void main(String[] args){
        Account.setActiveBlockchain(new KootenayKoinBlockchain());

        Miner miner = new Miner(1);

        miner.setKootenayKoin(new KootenayKoin());

        // populates accounts with random balances
        Transactions genesisTransactions = new Transactions();
        genesisTransactions.populateAccountBalances(miner);

        miner.createGenesisKoin("0".repeat(Miner.DIFFICULTY) + "this too shall pass",
                genesisTransactions,
                1);

        try {
            miner.mine(miner.getKootenayKoin());
        } catch (NoNonceFoundException | InvalidKootenayKoinException | InvalidTransactionException e) {
            e.printStackTrace();
        }


        for (int i = 1; i < 4; i++){
            KootenayKoin koin = new KootenayKoin(blockchain.get(i-1).hash(), Transactions.generateValidTransactions(miner), i, Miner.DIFFICULTY);
            try {
                koin = miner.mine(koin);
            } catch (NoNonceFoundException | InvalidKootenayKoinException | InvalidTransactionException e) {
                e.printStackTrace();
            }

        }

        System.out.println("Miner account Balance :" + miner.calculateBalance() + "₭");

        for (int i = 0; i < Account.amountOfAccounts; i++){
            System.out.println("Account #" + i + " Balance: " + calculateBalance(i) + "₭");
        }
    }
}
