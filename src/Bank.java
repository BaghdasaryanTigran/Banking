import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Bank implements ITransaction{
    private ArrayNode userAccounts;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String path = "src/resources/accounts.json";
    private final File jsonFile = new File(path);



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
    public void Transfer(String from, String to, double amount) throws InvalidTransactionException {
            JsonNode nodeFrom = GetNode(from);
            JsonNode nodeTo = GetNode(to);
            if(nodeFrom !=null && nodeTo != null && amount > 0){
                int balanceFrom = nodeFrom.get("balance").asInt();
                int balanceTo = nodeTo.get("balance").asInt();
                ((ObjectNode) nodeFrom).put("balance", balanceFrom - amount);
                ((ObjectNode) nodeTo).put("balance", balanceTo  + amount);
                JsonUpdate();
                return;
            }
        throw new InvalidTransactionException();
    }

    public void Withdraw(String accFrom , double withdrawAmount) throws InsufficientFundsException, FileNotFoundException, InvalidAmount {
        JsonNode node = GetNode(accFrom);
        if(node == null){
            throw new FileNotFoundException();
        }
        if(WithdrawPossibility( accFrom, withdrawAmount)) {
            double newBalance = GetBalance(accFrom);
            ((ObjectNode) node).put("balance", newBalance - withdrawAmount);
            JsonUpdate();
            return;
        }
        throw new UnknownError();
    }

    private boolean WithdrawPossibility(String accfrom, double money) throws InsufficientFundsException, InvalidAmount {
        if(money <= 0)
        {
            throw new InvalidAmount();
        }
        if ( GetBalance(accfrom)- money >= 0) {
            return true;
        }
        throw new InsufficientFundsException();
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

    public double GetBalance(String accountNumber){
        JsonNode node  = GetNode(accountNumber);
        double balance = node.get("balance").asInt();
        return balance;
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


}
