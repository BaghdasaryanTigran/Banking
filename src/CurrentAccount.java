import com.fasterxml.jackson.databind.JsonNode;

public class CurrentAccount extends BankAccount {
    Bank bank = new Bank();
    private int overDraftLimit ;

    @Override
    JsonNode displayAccountDetails(String accNumber) {
        return  bank.GetNode(accNumber);
    }

    public CurrentAccount(String accountNumber, String accountHolderName, double balance ) {
        super(accountNumber, accountHolderName, balance);
        overDraftLimit = -10000;
    }

    public int getOverDraftLimit() {
        return overDraftLimit;
    }

}
