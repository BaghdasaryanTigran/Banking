import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args)  {

        Bank bank = new Bank();
        boolean exit = false;
        while (!exit) {

            System.out.println("Choose one of the following options:");
            System.out.println("1. Create Account");
            System.out.println("2. View Account Details");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transfer Money");
            System.out.println("6. Get Account Balance");
            System.out.println("7. Calculate Interest (Savings Account)");
            System.out.println("8. Download Account Report");
            System.out.println("9. Exit");

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":// Creating an account//Complete
                    System.out.println("Please enter account holder's name: ");
                    String accountHolderName = scanner.nextLine();
                    String accountNumber;

                    String accountType;
                    while (true) {
                        System.out.println("Input Prefered Account Number");
                        accountNumber = scanner.nextLine();
                        System.out.println("Input Account Type: S-SavingAccount, C-CurrentAccount");
                        accountType = scanner.nextLine().toLowerCase();

                        if (accountType.equals("s") || accountType.equals("c")) {
                            try {
                                if (accountType.equals("s")) {
                                    bank.CreateSavingAccount(accountNumber, accountHolderName);
                                } else {
                                    bank.CreateCurrentAccount(accountNumber, accountHolderName);
                                }
                                break;
                            } catch (InvalidInput e) {
                                System.out.println("Account number already exists. Please try again.");
                            }
                        }else {
                            System.out.println("Wrong Type ");
                        }

                    }


                    System.out.println("Account created successfully for " + accountHolderName);
                    break;

                case "2":// View account details
                    String accountNumberDetails;
                    while (true) {
                        System.out.println("Please enter account number to view details: ");
                        accountNumberDetails = scanner.nextLine();
                        try {
                            System.out.println(bank.GetAccount(accountNumberDetails));
                            break;
                        }catch (WrongAccount e){
                            System.out.println("Invalid Account Number");
                        }catch (Exception e){
                            System.out.println("An unexpected error occurred. Please try again.");
                        }
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
                        } catch (WrongAccount e) {
                            System.out.println("Account Not Found. Please try again.");
                        } catch (InvalidAmount e) {
                            System.out.println("Amount must be greater than 0. Please try again.");
                        } catch (Exception e) {
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
                        } catch (InsufficientFundsException e ){
                            System.out.println("Insufficient Funds");
                        }
                        catch (Exception e) {
                            throw new RuntimeException();
                        }
                    }
                    System.out.println("Transferred " + transferAmount + " from account " + transferFromAccount + " to account " + transferToAccount);
                    break;
                case "6"://Get balance
                    String balanceAccNumber;
                    while(true){
                        System.out.println("Input Account Number");
                        balanceAccNumber = scanner.nextLine();
                        try {
                            System.out.println("Balance: " + bank.GetBalance(balanceAccNumber));
                            break;
                        } catch (WrongAccount e) {
                            System.out.println("WrongAccount");
                        }
                    }
                    break;
                case "7":// Calculate interest (Works Only With Saving Ones)
                    double interestRate;
                    String interestaccNumber;
                    while (true) {
                        System.out.println("Please enter account number to calculate interest:");
                        interestaccNumber = scanner.nextLine();
                        try {
                            interestRate = bank.CalculateInterest(interestaccNumber);
                            System.out.println("Interest calculated successfully.");
                            break;
                        } catch (WrongAccount e) {
                            System.out.println("Wrong Account. Please try again.");
                        } catch (Exception e) {
                            throw new RuntimeException();
                        }
                    }
                    System.out.println("The calculated interest is: " + interestRate);
                    break;
                case "8":
                    String reportAccNumber;
                    while (true) {
                        System.out.println("Please enter account number to view report: ");
                        reportAccNumber = scanner.nextLine();
                        try {
                           System.out.println(bank.GenerateReport(reportAccNumber));
                            break;
                        }catch (WrongAccount e){
                            System.out.println("Invalid Account Number");
                        }catch (Exception e){
                           System.out.println("An unexpected error occurred. Please try again.");
                        }
                    }
                    break;
                case "9":// Exit the program
                    System.out.println("Exiting...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }

        }

    }
}
