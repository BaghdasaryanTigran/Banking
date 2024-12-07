import com.google.gson.JsonElement;

import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws WrongAccount, InvalidTransactionException, InsufficientFundsException, LOL, InvalidInput {

        Bank bank = new Bank();
        boolean exit = false;
        while (!exit) {

            System.out.println("Choose one of the following options:");
            System.out.println("1. Create Account");
            System.out.println("2. View Account Details");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transfer Money");
            System.out.println("6. Calculate Interest (Savings Account)");
            System.out.println("7. Exit");
            Scanner scanner = new Scanner(System.in);

            String choice = scanner.nextLine();


            switch (choice) {
                case "1":
                    // Creating an account

                    System.out.println("Please enter account holder's name: ");
                    String accountHolderName = scanner.nextLine();
                    String accountNumber;
                    while (true){

                        System.out.println("Input Prefered Account Number");
                         accountNumber = scanner.nextLine();
                        if(!bank.IsAccountExist(accountNumber)){
                            break;
                        }
                        System.out.println("Acount Already Exist");
                    }

                    String accountType;
                        while (true) {

                            System.out.println("Input Account Type: S-SavingAcccount, C-CurrentAccount");
                            accountType = scanner.nextLine().toLowerCase();

                                if (accountType.equals("s") || accountType.equals("c")){
                                    switch (accountType){
                                        case "s":
                                            bank.CreateSavingAccount(accountNumber, accountHolderName);
                                            break;
                                        case "c":
                                            bank.CreateCurrentAccount(accountNumber, accountHolderName);
                                            break;
                                    }

                                    break;
                                }
                                System.out.println("Wrong Type ");
                        }
                    System.out.println("Account created successfully for " + accountHolderName);
                    break;

                case "2":
                    // View account details
                    String accountNumberDetails;
                    while (true) {

                        System.out.println("Please enter account number to view details: ");
                        accountNumberDetails = scanner.nextLine();
                        if(bank.IsAccountExist(accountNumberDetails)){
                            System.out.println(bank.GetNode(accountNumberDetails));
                            break;
                        }

                        System.out.println("Invalid Account Number");
                    }
                    break;

                case "3":
                    // Deposit money
                        System.out.println("Please enter account number: ");
                        String depositAccountNumber = scanner.nextLine();
                        System.out.println("Please enter the amount to deposit: ");
                        double moneyToDeposit = scanner.nextDouble();
                    try {
                        bank.DepositMoney(depositAccountNumber, moneyToDeposit);
                    } catch (InvalidTransactionException e) {}
                     catch (Exception e) {throw  new RuntimeException();}
                    System.out.println("Deposited " + moneyToDeposit + " into account " + depositAccountNumber);
                    break;

                case "4":
                    // Withdraw money
                    System.out.println("Please enter account number: ");
                    scanner.nextLine(); // consume newline
                    String withdrawAccountNumber = scanner.nextLine();
                    System.out.println("Please enter the amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    // Call method to withdraw money
                    System.out.println("Withdrew " + withdrawAmount + " from account " + withdrawAccountNumber);
                    break;

                case "5":
                    // Transfer money
                    System.out.println("Please enter source account number: ");
                    String transferFromAccount = scanner.nextLine();
                    System.out.println("Please enter destination account number: ");
                    String transferToAccount = scanner.nextLine();
                    System.out.println("Please enter the amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    bank.Transfer(transferFromAccount,transferToAccount,transferAmount);
                    System.out.println("Transferred " + transferAmount + " from account " + transferFromAccount + " to account " + transferToAccount);
                    break;

                case "6":
                    // Calculate interest for savings account
                    System.out.println("Please enter account number to calculate interest: ");

                    String accNumber = scanner.nextLine();
                    System.out.println("The calculated interest is: " + bank.CalculateInterest(accNumber));
                    break;

                case "7":
                    // Exit the program
                    System.out.println("Exiting...");
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice, please try again.");
            }

        }

    }

//        System.out.println(bank.Withdraw("12345", 1000));
//        System.out.println(bank.GetBalance("12345"));
//        System.out.println(bank.Transfer("12345", "98", 5000));
//        System.out.println(bank.GetBalance("12345"));
//        System.out.println(bank.GetBalance("98"));


 //       String accNumber;

//
//        System.out.println("Input Name");
//        String userName = scanner.nextLine();
//
//        System.out.println("Input Account Type");
//
//
//        bank.CreateAccount("aa",accNumber, userName);



}
