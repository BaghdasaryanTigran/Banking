import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;

public class Bank implements ITransaction{
    private ArrayNode userAccounts;
    ObjectMapper objectMapper = new ObjectMapper();
    private FileReader reader;
    private String path = "src/resources/accounts.json";
    private File jsonFile = new File(path);







    public boolean CreateCurrentAccount(String accNumber,String userName) throws InvalidInput {

        CurrentAccount currentAccount = new CurrentAccount(accNumber,userName,0 );
        if(IsAccountExist(accNumber)) {
           throw new InvalidInput();
        }
        JsonNode node = objectMapper.valueToTree(currentAccount);
        userAccounts.add(node);
        JsonUpdate();
        return true;

    }
    public boolean CreateSavingAccount(String accNumber,String userName) throws InvalidInput {

        SavingsAccount savingsAccount = new SavingsAccount(accNumber,userName,0 );
        if(IsAccountExist(accNumber)) {
           throw new InvalidInput();
        }
        JsonNode node = objectMapper.valueToTree(savingsAccount);
        userAccounts.add(node);
        JsonUpdate();
        return true;

    }
    public boolean DepositMoney(String accNumber, double money) throws InvalidTransactionException {
        if(IsAccountExist(accNumber) && money > 0) {
            JsonNode node = GetNode(accNumber);
            ((ObjectNode) node).put("balance", node.get("balance").asInt()  + money);
            JsonUpdate();
            return true;
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
        }

        else{
            return node.get("balance").asDouble() * node.get("interestRate").asInt() / 100;
        }


    }
    @Override
    public boolean Transfer(String from, String to, double amount) throws InvalidTransactionException {


            JsonNode nodeFrom = GetNode(from);
            JsonNode nodeTo = GetNode(to);
            if(nodeFrom !=null && nodeTo != null){
                int balanceFrom = nodeFrom.get("balance").asInt();
                int balanceTo = nodeTo.get("balance").asInt();
                ((ObjectNode) nodeFrom).put("balance", balanceFrom - amount);
                ((ObjectNode) nodeTo).put("balance", balanceTo  + amount);
                JsonUpdate();
                return true;
            }

        throw new InvalidTransactionException();
    }
    public boolean Withdraw(String accFrom , int withdrawAmount) throws InsufficientFundsException, LOL{
        JsonNode node = GetNode(accFrom);
        if(WithdrawPossibility( accFrom, withdrawAmount)) {
            double newBalance = GetBalance(accFrom);
            ((ObjectNode) node).put("balance", newBalance - withdrawAmount);
            JsonUpdate();
            return true;
        }
       throw new LOL();

    }

    private void JsonUpdate()
    {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, userAccounts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean WithdrawPossibility(String accfrom, int money)throws InsufficientFundsException {

        if ( GetBalance(accfrom)- money >= 0) {
            return true;
        }
        throw new InsufficientFundsException();
    }
    public JsonNode GetNode(String accNumber)
    {
       JsonNode node = null;

            for(JsonNode accountNode : userAccounts){
                String accountNumbersss = accountNode.get("accountNumber").asText();
                if(accountNumbersss.equals(accNumber)) {

                    node = accountNode;
                }
            }

        return node;
    }
    public CurrentAccount GetAccount(String accountNumber)
    {
        CurrentAccount acc = null;
        JsonNode node = GetNode(accountNumber);
        try {
            acc = objectMapper.treeToValue(node, CurrentAccount.class);
        } catch (JsonProcessingException e) {throw new RuntimeException(e);}
         catch (Exception e) {throw new RuntimeException();}

        return acc;
    }

    public double GetBalance(String accountNumber){
        JsonNode node  = GetNode(accountNumber);
        double balance = node.get("balance").asInt();

        return balance;
    }

    public boolean IsAccountExist(String accountNumber)
    {

        if(GetNode(accountNumber) != null){return true;}
        return false;
    }


    private  void ReadAccountsInfo() {
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            userAccounts = (ArrayNode) objectMapper.readTree(reader);

        } catch (Exception e){ throw new NullPointerException();}
    }
    public Bank() {

        //ReadAccounts();
        ReadAccountsInfo();
    }


}
