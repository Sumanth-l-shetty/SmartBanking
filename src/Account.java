
public class Account {

    // Account details
    private String accountNumber;
    private String accountHolderName;
    private double balance;

    // Constructor
    public Account(String accountNumber, String accountHolderName, double balance) {

        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    // Get account number
    public String getAccountNumber() {
        return accountNumber;
    }

    // Get account holder name
    public String getAccountHolderName() {
        return accountHolderName;
    }

    // Get balance
    public synchronized double getBalance() {
        return balance;
    }

    // Deposit money
    public synchronized void deposit(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Deposit amount must be greater than zero."
            );
        }

        balance += amount;
    }

    // Withdraw money
    // Withdraw money
public synchronized void withdraw(double amount)
        throws InsufficientBalanceException {

    if (amount <= 0) {
        throw new IllegalArgumentException(
            "Withdrawal amount must be greater than zero."
        );
    }

    if (amount > balance) {
        throw new InsufficientBalanceException(
            "Insufficient account balance."
        );
    }

    balance -= amount;
}}