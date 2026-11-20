
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private String accountNumber;
    private String transactionType;
    private double amount;
    private double balanceAfter;
    private String dateTime;

    // Constructor for new transactions
    public Transaction(
        String accountNumber,
        String transactionType,
        double amount,
        double balanceAfter
    ) {

        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(
                "dd-MM-yyyy HH:mm:ss"
            );

        this.dateTime = LocalDateTime.now()
            .format(formatter);
    }

    // Constructor for loading saved transactions
    public Transaction(
        String accountNumber,
        String transactionType,
        double amount,
        double balanceAfter,
        String dateTime
    ) {

        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime = dateTime;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String toFileString() {

        return accountNumber + "|" +
               transactionType + "|" +
               amount + "|" +
               balanceAfter + "|" +
               dateTime;
    }
}
