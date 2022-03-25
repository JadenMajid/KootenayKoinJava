package Java;

import java.util.LinkedList;

public class KootenayKoinBlockchain {
    LinkedList<KootenayKoin> blockchain = new LinkedList<>();

    public KootenayKoinBlockchain(){
        blockchain = new LinkedList<>();
        return;
    }
    public void addKootenayKoin(KootenayKoin koin){
        blockchain.add(koin);
    }

    // Adds Koin to current blockchain
    public void addKoinToChain(KootenayKoin koin){
        try {
            if (koin.validate()){
                blockchain.add(koin);
            }
        } catch(InvalidKootenayKoinException e) {
            System.out.println("Invalid Kootenay Koin detected: \n" + koin);
        }
    }

    public KootenayKoin getBlock(int i){
        return blockchain.get(i);
    }

    // Validates blockchain
    public boolean validate() throws InvalidKootenayKoinException {
        // checks genesis block
        blockchain.get(0).validate();

        //
        for (int i = 1; i < blockchain.size(); i++){
            try {
                blockchain.get(i).validate(blockchain.get(i-1).getPreviousHash());
            } catch(InvalidKootenayKoinException e) {
                System.out.println("invalid blockchain");
                return false;
            }
        }
        return true;
    }
}


