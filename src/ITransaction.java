public interface ITransaction  {
    boolean Transfer(String from, String to, double amount) throws InvalidTransactionException, WrongAccount;
}
