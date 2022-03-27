package Java;

public class Miner extends Account {
    public static int DIFFICULTY = 5;
    private KootenayKoin coin; // Current coin miner is mining

    public Miner() {
        this.coin = null;
    }

    public Miner(int address) {
        super(address);
        this.coin = null;
    }

    public KootenayKoin getBlock(int i) {
        return blockchain.getBlock(i);
    }

    public void makeKootenayKoin() { // Set method for Miner.coin
        this.coin = new KootenayKoin(blockchain.getBlock(KootenayKoinBlockchain.getSize()).hash(),
                this.coin.getTransactions().generateValidTransactions(),
                0, DIFFICULTY);
    }

    public void setKootenayKoin(KootenayKoin coin) {
        this.coin = coin;
    }

    public KootenayKoin getKootenayKoin() {
        return this.coin;
    }

    public void mine(KootenayKoin noNonceKoin)
            throws NoNonceFoundException, InvalidTransactionException, InvalidKootenayKoinException {
        int nonce = 0;
        String value = "";

        noNonceKoin.setLastTransaction(new Transaction(10, getAddress(), Account.miningRewarder));

        while (nonce < Integer.MAX_VALUE) {
            noNonceKoin.setNonce(nonce);
            value = noNonceKoin.hash();

            if (value.substring(0, Miner.DIFFICULTY).equals("0".repeat(Miner.DIFFICULTY))) {

                noNonceKoin.validate();
                blockchain.addKoinToChain(noNonceKoin);
                return;
            }
            nonce++;
        }

        // No working nonce found indicating something wrong happened, throw an
        // exception
        throw new NoNonceFoundException(this.coin);
    }

    public KootenayKoin createGenesisKoin(String genesisString, Transactions transactions) {
        return KootenayKoin.createGenesisKoin(genesisString, transactions);
    }
    /*
     * public boolean validateTransactions(){
     * try {
     * this.coin.getTransactions().validate(blockchain);
     * } catch(InvalidTransactionException e){
     * System.out.println("Invalid Transaction: \n");
     * }
     * return true;
     * }
     */
}
