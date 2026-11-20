
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class FileManager {

    private static final String FILE_PATH =
        "data/accounts.txt";

    // Save all accounts to file
    public static void saveAccounts(ArrayList<Account> accounts) {

        try {
            Path path = Paths.get(FILE_PATH);

            // Create data folder if it does not exist
            Files.createDirectories(path.getParent());

            BufferedWriter writer =
                Files.newBufferedWriter(path);

            for (Account account : accounts) {

                writer.write(
                    account.getAccountNumber() + "|" +
                    account.getAccountHolderName() + "|" +
                    account.getBalance()
                );

                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            throw new UncheckedIOException(
                "Unable to save account data.", e
            );
        }
    }

    // Load accounts from file
    public static ArrayList<Account> loadAccounts() {

        ArrayList<Account> accounts = new ArrayList<>();

        Path path = Paths.get(FILE_PATH);

        if (!Files.exists(path)) {
            return accounts;
        }

        try (BufferedReader reader =
                 Files.newBufferedReader(path)) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length != 3) {
                    continue;
                }

                String accountNumber = data[0];
                String name = data[1];
                double balance = Double.parseDouble(data[2]);

                Account account = new Account(
                    accountNumber, name, balance
                );

                accounts.add(account);
            }

        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(
                "Unable to load account data.", e
            );
        }

        return accounts;
    }

public static void saveTransaction(Transaction transaction) {

    try {
        Path path = Paths.get("data/transactions.txt");

        Files.createDirectories(path.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write(transaction.toFileString());
            writer.newLine();
        }

    } catch (IOException e) {
        throw new UncheckedIOException(
            "Unable to save transaction.", e
        );
    }
}

public static ArrayList<Transaction> loadTransactions() {

    ArrayList<Transaction> transactions =
        new ArrayList<>();

    Path path = Paths.get("data/transactions.txt");

    if (!Files.exists(path)) {
        return transactions;
    }

    try (BufferedReader reader =
             Files.newBufferedReader(path)) {

        String line;

        while ((line = reader.readLine()) != null) {

            String[] data = line.split("\\|", -1);

            if (data.length != 5) {
                continue;
            }

            Transaction transaction = new Transaction(
                data[0],
                data[1],
                Double.parseDouble(data[2]),
                Double.parseDouble(data[3]),
                data[4]
            );

            transactions.add(transaction);
        }

    } catch (IOException | NumberFormatException e) {

        throw new RuntimeException(
            "Unable to load transaction history.", e
        );
    }

    return transactions;
}


}
