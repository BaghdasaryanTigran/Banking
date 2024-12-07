import com.fasterxml.jackson.databind.JsonNode;

abstract class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private double balance;


    //Useless thing, is here only bc of assignment
    abstract JsonNode displayAccountDetails(String accNumber);
    public void Deposit(double depositMoney) {
        balance += depositMoney;
    }

    public double getBalance(){
        return balance;
    }

    public void setBalance(double amount)
    {
        this.balance += amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public BankAccount(String   accountNumber, String accountHolderName, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;

    }

}
