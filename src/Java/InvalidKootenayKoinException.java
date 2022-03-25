package Java;

class InvalidKootenayKoinException extends Exception{
    public InvalidKootenayKoinException(String s, KootenayKoin koin){
        super(s);
        System.out.println("Invalid blockchain, Koin:" + koin);
    }
}
