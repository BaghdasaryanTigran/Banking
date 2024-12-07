public class WrongAccount extends Exception {
    public WrongAccount(){
        super("Provided Account does not Exist or is Not Saving");
    }
}
