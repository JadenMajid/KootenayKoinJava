package Java;

import static Java.KootenayKoinBlockchain.blockchain;
import static Java.KootenayKoinBlockchain.calculateBalance;

public class Main {
    public static void main(String[] args) {
        Account.setActiveBlockchain(new KootenayKoinBlockchain());

        Miner miner = new Miner(1);

        KootenayKoin temporaryCoin = new KootenayKoin();

        // populates accounts with random balances
        Transactions temporaryTransactions = new Transactions();
        temporaryTransactions.populateAccountBalances();

        miner.createGenesisKoin("Let there be Light", temporaryTransactions);

        for (int i = 0; i < 4; i++) {
            temporaryTransactions.generateValidTransactions();

            KootenayKoin koin = new KootenayKoin(blockchain.get(i).hash(),
                    temporaryTransactions, i + 1, Miner.DIFFICULTY);
            try {
                miner.mine(koin);
            } catch (NoNonceFoundException | InvalidKootenayKoinException | InvalidTransactionException e) {
                e.printStackTrace();
            }

        }

        System.out.println("Miner account Balance :" + miner.calculateBalance() + "₭");

        for (int i = 0; i < Account.amountOfAccounts; i++) {
            System.out.println("Account #" + i + " Balance: " + calculateBalance(i) + "₭");
        }
    }
}
