import java.io.FileNotFoundException;

public interface ITransaction  {
    void Transfer(String from, String to, double amount) throws InvalidTransactionException, WrongAccount, InvalidAmount, InsufficientFundsException, FileNotFoundException;
}
