import com.fasterxml.jackson.databind.JsonNode;


     public class SavingsAccount extends BankAccount {

        private double interestRate;

         public SavingsAccount(String accountNumber, String accountHolderName, double balance) {
             super(accountNumber, accountHolderName, balance);
             this.interestRate = 9 ;
         }


         public double getInterestRate() {
            return interestRate;
        }

        @Override
        JsonNode displayAccountDetails(String accNumber) {
            return null;
        }

    }


