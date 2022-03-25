package Java;

class TransactionAdditionException extends Exception{
    public TransactionAdditionException(){
        super("EXCEPTION: Transaction could not be added");
    }
}
