package Java;

public class Miner {
    public int address;
    public int DIFFICULTY = 3;
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

        while (!foundNonce && nonce < Integer.MAX_VALUE){
            nonce++;
            noNonceKoin.setNonce(nonce);

            value = noNonceKoin.hash();

            if (value.substring(0,DIFFICULTY).equals("0".repeat(DIFFICULTY))){
                System.out.println("\n==========MINTED COIN==========\n" + noNonceKoin + "\n===============================");
                blockchain.addKoinToChain(noNonceKoin);
                foundNonce = true;
                return noNonceKoin;
            }
        }

        noNonceKoin.setNonce(0);
        return noNonceKoin;
    }

    public void createGenesisKoin(String genesisString, Transactions transactions, int difficulty) {
        coin = KootenayKoin.createGenesisKoin(genesisString, transactions, difficulty);
    }
}
