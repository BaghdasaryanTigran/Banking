import com.google.gson.JsonElement;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InvalidInput {

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
            System.out.println("7. Download Account Report");

            System.out.println("8. Exit");

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":// Creating an account//Complete
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

                case "2":// View account details
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

                case "3":// Deposit money
                    String depositAccountNumber;
                    double moneyToDeposit;
                    while (true) {
                        try {
                            System.out.println("Please enter account number:");
                            depositAccountNumber = scanner.nextLine();
                            while (true) {
                                System.out.println("Please enter the amount to Deposit:");
                                try {
                                    moneyToDeposit = Double.parseDouble(scanner.nextLine());
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a numeric value.");
                                }
                            }
                            bank.DepositMoney(depositAccountNumber, moneyToDeposit);
                            System.out.println("Deposit successful.");
                            break;
                        } catch (InvalidTransactionException e) {
                            System.out.println("Invalid Inputs. Please try again.");
                        } catch (Exception e) {
                            System.out.println("An unexpected error occurred. Please try again.");
                        }
                    }
                    System.out.println("Deposited " + moneyToDeposit + " into account " + depositAccountNumber);
                    break;

                case "4": // Withdraw money
                    String withdrawaccNumber;
                    double withdrawAmount;
                    while (true) {
                        try {
                            System.out.println("Please enter account number:");
                            withdrawaccNumber = scanner.nextLine();
                            while (true) {
                                System.out.println("Please enter the amount to withdraw:");
                                try {
                                    withdrawAmount = Double.parseDouble(scanner.nextLine());
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a numeric value.");
                                }
                            }
                            bank.Withdraw(withdrawaccNumber, withdrawAmount);
                            System.out.println("Withdrawal successful.");
                            break;
                        } catch (InsufficientFundsException e) {
                            System.out.println("Insufficient funds. Please try again.");
                        } catch (FileNotFoundException e) {
                            System.out.println("Account Not Found. Please try again.");
                        }catch (InvalidAmount e) {
                                System.out.println("Amount must be greater than 0. Please try again.");
                            }
                        catch (Exception e) {
                            System.out.println("An unexpected error occurred. Please try again.");
                        }
                    }
                    System.out.println("Withdrew " + withdrawAmount + " from account " + withdrawaccNumber);
                    break;

                case "5":// Transfer money
                    String transferFromAccount;
                    String transferToAccount;
                    double transferAmount;
                    while (true) {
                        try {
                            System.out.println("Please enter source account number:");
                            transferFromAccount = scanner.nextLine();

                            System.out.println("Please enter destination account number:");
                            transferToAccount = scanner.nextLine();

                            while (true) {
                                System.out.println("Please enter the amount to transfer:");
                                try {
                                    transferAmount = Double.parseDouble(scanner.nextLine());
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a numeric value.");
                                }
                            }

                            bank.Transfer(transferFromAccount, transferToAccount, transferAmount);
                            System.out.println("Transaction completed successfully.");
                            break;
                        } catch (InvalidTransactionException e) {
                            System.out.println("Invalid transaction. Please try again.");
                        } catch (Exception e) {
                            throw new RuntimeException();
                        }
                    }
                    System.out.println("Transferred " + transferAmount + " from account " + transferFromAccount + " to account " + transferToAccount);
                    break;

                case "6":// Calculate interest (Works Only With Saving Ones)
                    double interestRate;
                    String interestaccNumber;
                    while (true) {
                        System.out.println("Please enter account number to calculate interest:");
                        interestaccNumber = scanner.nextLine();
                        try {
                           interestRate =  bank.CalculateInterest(interestaccNumber);
                            System.out.println("Interest calculated successfully.");
                            break;
                        } catch (WrongAccount e) {
                            System.out.println("Wrong Account. Please try again.");
                        }catch (Exception e){
                            throw new RuntimeException();
                        }
                    }
                    System.out.println("The calculated interest is: " + interestRate);
                    break;

                case "8":// Exit the program
                    System.out.println("Exiting...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }

        }

    }
    //    public CurrentAccount GetAccount(String accountNumber) {
//        CurrentAccount acc = null;
//        JsonNode node = GetNode(accountNumber);
//        try {
//            acc = objectMapper.treeToValue(node, CurrentAccount.class);
//        } catch (JsonProcessingException e) {throw new RuntimeException(e);}
//         catch (Exception e) {throw new RuntimeException();}
//
//        return acc;
//    }


}
