

import java.util.ArrayList;

public class BankService {

    private ArrayList<Account> accounts;

    private int nextAccountNumber = 1001;

    // Load previously saved accounts
    public BankService() {

        accounts = FileManager.loadAccounts();

        // Find the next available account number
        for (Account account : accounts) {

            String number = account.getAccountNumber();

            if (number.startsWith("NIT")) {

                try {
                    int value = Integer.parseInt(
                        number.substring(3)
                    );

                    if (value >= nextAccountNumber) {
                        nextAccountNumber = value + 1;
                    }

                } catch (NumberFormatException e) {
                    // Ignore invalid account number
                }
            }
        }
    }

    // Create a new account
    public Account createAccount(
        String name, double initialBalance) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Customer name cannot be empty."
            );
        }

        if (initialBalance < 0) {
            throw new IllegalArgumentException(
                "Initial balance cannot be negative."
            );
        }

        String accountNumber =
            "NIT" + nextAccountNumber++;

        Account account = new Account(
            accountNumber,
            name.trim(),
            initialBalance
        );

        accounts.add(account);

        saveAccounts();

        return account;
    }

    // Find an account
    public Account findAccount(String accountNumber) {

        for (Account account : accounts) {

            if (account.getAccountNumber()
                    .equalsIgnoreCase(accountNumber)) {
                return account;
            }
        }

        return null;
    }

    // Get all accounts
    public ArrayList<Account> getAllAccounts() {
        return accounts;
    }

    // Save accounts
    public void saveAccounts() {
        FileManager.saveAccounts(accounts);
    }
}
