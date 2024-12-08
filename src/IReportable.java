public interface IReportable {
    String GenerateReport(String accNumber) throws WrongAccount;
}
