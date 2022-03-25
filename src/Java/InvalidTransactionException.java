package Java;

public class InvalidTransactionException extends Exception{
    InvalidTransactionException(String s){
        super("Invalid Transaction: "+ s);
    }
}
