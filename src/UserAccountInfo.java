import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccountInfo  {
    private String accountNumber;
    private String accountHolderName;
    private double balance;


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


}
