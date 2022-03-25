package Java;

class NoNonceFoundException extends Exception{
    public NoNonceFoundException(KootenayKoin koin){
        super("No suitable nonce found, Koin:" + koin);
    }
}
