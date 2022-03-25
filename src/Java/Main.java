package Java;

public class Main{
    public static void main(String[] args){
        Miner miner = new Miner(1);

        miner.makeKootenayKoin();
        miner.createGenesisKoin("0this too shall pass",
                Transactions.generateTransactions(),
                1);
        miner.mine(miner.coin);
        for (int i = 1; i < 4; i++){
            KootenayKoin koin = new KootenayKoin(miner.getBlock(i-1).hash(), Transactions.generateTransactions(), i, miner.DIFFICULTY);
            koin = miner.mine(koin);
        }
    }
}
