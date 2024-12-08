import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Bank implements ITransaction, IReportable{
    private ArrayNode userAccounts;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String path = "src/resources/accounts.json";
    private File jsonFile = new File(path);


    public void CreateCurrentAccount(String accNumber,String userName) throws InvalidInput {
        CurrentAccount currentAccount = new CurrentAccount(accNumber,userName,0 );
        if(IsAccountExist(accNumber)) {
           throw new InvalidInput();
        }
        JsonNode node = objectMapper.valueToTree(currentAccount);
        userAccounts.add(node);
        JsonUpdate();
    }

    public void CreateSavingAccount(String accNumber,String userName) throws InvalidInput {
        SavingsAccount savingsAccount = new SavingsAccount(accNumber,userName,0 );
        if(IsAccountExist(accNumber)) {
           throw new InvalidInput();
        }
        JsonNode node = objectMapper.valueToTree(savingsAccount);
        userAccounts.add(node);
        JsonUpdate();
    }

    public void DepositMoney(String accNumber, double money) throws InvalidTransactionException {
        if (IsAccountExist(accNumber) && money > 0) {
            JsonNode node = GetNode(accNumber);
            ((ObjectNode) node).put("balance", node.get("balance").asInt() + money);
            JsonUpdate();
            return;
        }
        throw new InvalidTransactionException();

    }

    public double CalculateInterest(String accNumber) throws WrongAccount{
        if(!IsAccountExist(accNumber)){
            throw new WrongAccount();
        }
        JsonNode node = GetNode(accNumber);
        if( node.get("interestRate") == null){
            throw new WrongAccount();
        } else{
            return node.get("balance").asDouble() * node.get("interestRate").asInt() / 100;
        }
    }

    @Override
    public void Transfer(String from, String to, double amount) throws InvalidTransactionException, InvalidAmount, InsufficientFundsException, FileNotFoundException, WrongAccount {
            JsonNode nodeFrom = GetNode(from);
            JsonNode nodeTo = GetNode(to);
            if(nodeFrom !=null && nodeTo != null && amount > 0){
                Withdraw(from,amount);
                DepositMoney(to,amount);
                JsonUpdate();
                return;
            }
        throw new InvalidTransactionException();
    }

    public void Withdraw(String accFrom , double withdrawAmount) throws InsufficientFundsException, InvalidAmount, WrongAccount {
        JsonNode node = GetNode(accFrom);
        if(node == null){
            throw new WrongAccount();
        }
        if(WithdrawPossibility(node, withdrawAmount)) {
            double newBalance = GetBalance(accFrom);
            ((ObjectNode) node).put("balance", newBalance - withdrawAmount);
            JsonUpdate();
            return;
        }
        throw new UnknownError();
    }

    private boolean WithdrawPossibility(JsonNode node, double money) throws InsufficientFundsException, InvalidAmount {
        if(money <= 0)
        {
            throw new InvalidAmount();
        }
        if(node.has("overDraftLimit")){
            if(node.get("balance").asInt() - money >= node.get("overDraftLimit").asInt()){
                return true;
            }
        }
        if ( node.get("balance").asInt()- money >= 0) {
            return true;
        }
        throw new InsufficientFundsException();
    }
        public String GetAccount(String accountNumber) throws WrongAccount {
        UserAccountInfo acc;
        JsonNode node = GetNode(accountNumber);
            if (node == null) {
                throw new WrongAccount();
            }

            try {
                acc = objectMapper.treeToValue(node, UserAccountInfo.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            String info = String.format("Account Number: %s\nAccount Holder: %s\nBalance: $%.2f",
                    acc.getAccountNumber(),
                    acc.getAccountHolderName(),
                    acc.getBalance());

            return info;
    }
    public JsonNode GetNode(String accNumber) {
       JsonNode node = null;

            for(JsonNode accountNode : userAccounts){
                String accountNumbersss = accountNode.get("accountNumber").asText();
                if(accountNumbersss.equals(accNumber)) {

                    node = accountNode;
                }
            }

        return node;
    }

    public double GetBalance(String accountNumber) throws WrongAccount {
        if(IsAccountExist(accountNumber)){
            JsonNode node  = GetNode(accountNumber);
            return node.get("balance").asDouble();
        }
        throw new WrongAccount();
    }

    public boolean IsAccountExist(String accountNumber) {
        if(GetNode(accountNumber) != null){return true;}
        return false;
    }

    private  void ReadAccountsInfo() {
        FileReader reader;
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            userAccounts = (ArrayNode) objectMapper.readTree(reader);

        } catch (Exception e){ throw new NullPointerException();}
    }

    private void JsonUpdate() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, userAccounts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bank() {

        ReadAccountsInfo();
    }


    @Override
    public String GenerateReport(String accNumber) throws WrongAccount {
        JsonNode node = GetNode(accNumber);
        if (node == null) {
            throw new WrongAccount();
        }

        String accountNumber = node.get("accountNumber").asText();
        String accountHolder = node.get("accountHolderName").asText();
        double balance = node.get("balance").asDouble();
        double interestRate = node.has("interestRate") ? node.get("interestRate").asDouble() : 0.0;
        double overdraftLimit = node.has("overDraftLimit") ? node.get("overDraftLimit").asDouble() : 0.0;

        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append(String.format("\nAccount Number:" + accountNumber));
        reportBuilder.append(String.format("\nAccount Holder: " +  accountHolder));
        reportBuilder.append(String.format("\nBalance: " + balance));

        if (interestRate > 0.0) {
            reportBuilder.append(String.format("\nInterest Rate:" + interestRate));
        }
        if (overdraftLimit < 0) {
            reportBuilder.append(String.format("\nOverdraft Limit:" + overdraftLimit));
        }

        return reportBuilder.toString();

    }

}
