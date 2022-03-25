package Java;

    public int DIFFICULTY = 3;

    public Miner(){
        this.coin = null;
    }

    public Miner(int address){
        super(address);
        this.coin = null;
    }

    public Miner(int address, KootenayKoinBlockchain blockchain){
        this.address = address;
        this.blockchain = blockchain;
        this.coin = null;
    }

    public KootenayKoin getBlock(int i){
        return blockchain.getBlock(i);
    }

    public void makeKootenayKoin(){ // Set method for Miner.coin
        this.coin = new KootenayKoin();
    }

    public KootenayKoin getKootenayKoin(){
        return this.coin;
    }

    public KootenayKoin mine(KootenayKoin noNonceKoin) throws NoNonceFoundException {
        int nonce = 0;
        String value = "";

        while (nonce < Integer.MAX_VALUE){
            noNonceKoin.setNonce(nonce);

            value = noNonceKoin.hash();

            if (value.substring(0,DIFFICULTY).equals("0".repeat(DIFFICULTY))){
                System.out.println("\n==========MINTED COIN==========\n" + noNonceKoin + "\n===============================");
                blockchain.addKoinToChain(noNonceKoin);
                return noNonceKoin;
            }

            nonce++;
        }

        throw new NoNonceFoundException(this.coin);
    }

    public void createGenesisKoin(String genesisString, Transactions transactions, int difficulty) {
        this.coin = KootenayKoin.createGenesisKoin(genesisString, transactions, difficulty);
    }

    public boolean validateTransactions() throws InvalidTransactionException{
        for (int i = 0; i < KootenayKoin.transactionsPerKoin; i++){
            if (Account.calculateBalance(coin.getTransactions().getTransaction(i).getAddressFrom(), this.blockchain) <
            }
        }
        return true;
    }
}
