
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

public class Main extends Application {
    private BankService bankService = new BankService();
    @Override
    public void start(Stage stage) {

        // Heading
       Label title = new Label("BANK OF NITTE");
        title.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1e3a8a;"
        );

        Label subtitle = new Label(
    "Smart Banking Management System"
);
        

        // Menu buttons
        Button createBtn = new Button("Create Account");
        Button depositBtn = new Button("Deposit Money");
        Button withdrawBtn = new Button("Withdraw Money");
        Button balanceBtn = new Button("Check Balance");
        Button historyBtn = new Button("Transaction History");
        Button exitBtn = new Button("Exit");
        Button viewAccountsBtn = new Button("View All Accounts");

        Button[] buttons = {
            createBtn, depositBtn, withdrawBtn,
            balanceBtn, historyBtn, viewAccountsBtn, exitBtn
        };

        for (Button button : buttons) {
            button.setPrefSize(240, 50);

            button.setStyle(
                "-fx-background-color: #2563eb;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;"
            );
        }

        // Temporary button actions
        createBtn.setOnAction(e -> 
            showCreateAccount(stage));

        depositBtn.setOnAction(e ->
    showDepositMoney(stage)
);

       withdrawBtn.setOnAction(e ->
    showWithdrawMoney(stage)
);

       balanceBtn.setOnAction(e ->
    showBalanceEnquiry(stage)
);

       historyBtn.setOnAction(e ->
    showTransactionHistory(stage)
);
        viewAccountsBtn.setOnAction(e -> showAllAccounts(stage));
        exitBtn.setOnAction(e -> stage.close());

        // Button layout
        GridPane menu = new GridPane();
        menu.setAlignment(Pos.CENTER);
        menu.setHgap(20);
        menu.setVgap(20);

        menu.add(createBtn, 0, 0);
        menu.add(depositBtn, 1, 0);
        menu.add(withdrawBtn, 0, 1);
        menu.add(balanceBtn, 1, 1);
        menu.add(historyBtn, 0, 2);
        menu.add(viewAccountsBtn, 1, 2);
        menu.add(exitBtn, 0,3);

        // Main layout
        VBox layout = new VBox(
            25, title, subtitle, menu
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        layout.setStyle(
            "-fx-background-color: #eff6ff;"
        );

        Scene scene = new Scene(layout, 750, 600);

        stage.setTitle("Bank of Nitte");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

private void showCreateAccount(Stage stage) {

    Label heading = new Label("CREATE NEW ACCOUNT");
    heading.setStyle(
        "-fx-font-size: 22px;" +
        "-fx-font-weight: bold;"
    );

    TextField nameField = new TextField();
    nameField.setPromptText("Enter customer name");

    TextField amountField = new TextField();
    amountField.setPromptText("Enter initial deposit");

    Button createButton = new Button("Create Account");
    Button backButton = new Button("Back");

    Label message = new Label();

   createButton.setOnAction(e -> {

    try {

        String name = nameField.getText().trim();

        String amountText = amountField.getText().trim();

        // Check empty name
        if (name.isEmpty()) {
            throw new IllegalArgumentException(
                "Please enter customer name."
            );
        }

        // Check name contains only letters and spaces
        if (!name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException(
                "Name should contain only letters."
            );
        }

        // Check empty amount
        if (amountText.isEmpty()) {
            throw new IllegalArgumentException(
                "Please enter initial deposit."
            );
        }

        double amount = Double.parseDouble(amountText);

        // Check negative amount
        if (amount < 0) {
            throw new IllegalArgumentException(
                "Initial deposit cannot be negative."
            );
        }

        Account account = bankService.createAccount(
            name, amount
        );

        message.setText(
            "Account Created Successfully!\n" +
            "Account Number: " +
            account.getAccountNumber()
        );

    } catch (NumberFormatException ex) {

        message.setText("Please enter a valid amount.");

    } catch (IllegalArgumentException ex) {

        message.setText(ex.getMessage());
    }
});

    backButton.setOnAction(e -> start(stage));

    VBox layout = new VBox(
        15, heading, nameField, amountField,
        createButton, backButton, message
    );

    layout.setPadding(new Insets(30));
    layout.setAlignment(Pos.CENTER);

    Scene scene = new Scene(layout, 500, 450);

    stage.setScene(scene);
    stage.setTitle("Bank of Nitte - Create Account");
    stage.show();
}

private void showDepositMoney(Stage stage) {

    Label heading = new Label("DEPOSIT MONEY");
    heading.setStyle(
        "-fx-font-size: 22px;" +
        "-fx-font-weight: bold;"
    );

    TextField accountField = new TextField();
    accountField.setPromptText("Enter account number");

    TextField amountField = new TextField();
    amountField.setPromptText("Enter deposit amount");

    Button depositButton = new Button("Deposit");
    Button backButton = new Button("Back");

    Label message = new Label();

   
depositButton.setOnAction(e -> {

    try {

        String accountNumber =
            accountField.getText().trim();

        String amountText =
            amountField.getText().trim();

        // Check empty account number
        if (accountNumber.isEmpty()) {
            throw new IllegalArgumentException(
                "Please enter account number."
            );
        }

        // Check empty amount
        if (amountText.isEmpty()) {
            throw new IllegalArgumentException(
                "Please enter deposit amount."
            );
        }

        // Find account
        Account account =
            bankService.findAccount(accountNumber);

        if (account == null) {
            message.setText("Account not found!");
            return;
        }

        double amount = Double.parseDouble(amountText);

        // Validate deposit amount
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Deposit amount must be greater than zero."
            );
        }

        // Deposit money
        account.deposit(amount);

        // Save updated account
        bankService.saveAccounts();

        // Save transaction
        FileManager.saveTransaction(
            new Transaction(
                accountNumber,
                "Deposit",
                amount,
                account.getBalance()
            )
        );

        message.setText(
            "Deposit Successful!\n" +
            "Account: " + accountNumber +
            "\nDeposited: Rs. " + amount +
            "\nNew Balance: Rs. " +
            account.getBalance()
        );

        amountField.clear();

    } catch (NumberFormatException ex) {

        message.setText(
            "Please enter a valid amount."
        );

    } catch (IllegalArgumentException ex) {

        message.setText(ex.getMessage());
    }
});


    backButton.setOnAction(e -> start(stage));

    VBox layout = new VBox(
        15, heading, accountField, amountField,
        depositButton, backButton, message
    );

    layout.setPadding(new Insets(30));
    layout.setAlignment(Pos.CENTER);

    Scene scene = new Scene(layout, 500, 450);

    stage.setScene(scene);
    stage.setTitle("Bank of Nitte - Deposit Money");
    stage.show();
}

private void showWithdrawMoney(Stage stage) {

    Label heading = new Label("WITHDRAW MONEY");

    heading.setStyle(
        "-fx-font-size: 22px;" +
        "-fx-font-weight: bold;"
    );

    TextField accountField = new TextField();
    accountField.setPromptText("Enter account number");

    TextField amountField = new TextField();
    amountField.setPromptText("Enter withdrawal amount");

    Button withdrawButton = new Button("Withdraw");
    Button backButton = new Button("Back");

    Label message = new Label();

  
withdrawButton.setOnAction(e -> {

    try {

        String accountNumber =
            accountField.getText().trim();

        String amountText =
            amountField.getText().trim();

        // Check empty account number
        if (accountNumber.isEmpty()) {
            throw new IllegalArgumentException(
                "Please enter account number."
            );
        }

        // Check empty amount
        if (amountText.isEmpty()) {
            throw new IllegalArgumentException(
                "Please enter withdrawal amount."
            );
        }

        // Find account
        Account account =
            bankService.findAccount(accountNumber);

        if (account == null) {
            message.setText("Account not found!");
            return;
        }

        double amount = Double.parseDouble(amountText);

        // Validate withdrawal amount
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Withdrawal amount must be greater than zero."
            );
        }

        // Withdraw money
        account.withdraw(amount);

        // Save updated account
        bankService.saveAccounts();

        // Save transaction
        FileManager.saveTransaction(
            new Transaction(
                accountNumber,
                "Withdrawal",
                amount,
                account.getBalance()
            )
        );

        message.setText(
            "Withdrawal Successful!\n" +
            "Account: " + accountNumber +
            "\nWithdrawn: Rs. " + amount +
            "\nRemaining Balance: Rs. " +
            account.getBalance()
        );

        amountField.clear();
} catch (NumberFormatException ex) {

    message.setText("Please enter a valid amount.");

} catch (IllegalArgumentException ex) {

    message.setText(ex.getMessage());

} catch (InsufficientBalanceException ex) {

    message.setText(ex.getMessage());
}
});

    backButton.setOnAction(e -> start(stage));

    VBox layout = new VBox(
        15, heading, accountField, amountField,
        withdrawButton, backButton, message
    );

    layout.setPadding(new Insets(30));
    layout.setAlignment(Pos.CENTER);

    Scene scene = new Scene(layout, 500, 450);

    stage.setScene(scene);
    stage.setTitle("Bank of Nitte - Withdraw Money");
    stage.show();
}


private void showTransactionHistory(Stage stage) {

    Stage historyStage = new Stage();

    Label heading = new Label(
        "BANK OF NITTE - TRANSACTION HISTORY"
    );

    heading.setStyle(
        "-fx-font-size: 22px;" +
        "-fx-font-weight: bold;"
    );

    TableView<Transaction> table =
        new TableView<>();

    TableColumn<Transaction, String> accountCol =
        new TableColumn<>("Account Number");

    accountCol.setCellValueFactory(
        new PropertyValueFactory<>("accountNumber")
    );

    TableColumn<Transaction, String> typeCol =
        new TableColumn<>("Transaction Type");

    typeCol.setCellValueFactory(
        new PropertyValueFactory<>("transactionType")
    );

    TableColumn<Transaction, Double> amountCol =
        new TableColumn<>("Amount");

    amountCol.setCellValueFactory(
        new PropertyValueFactory<>("amount")
    );

    TableColumn<Transaction, Double> balanceCol =
        new TableColumn<>("Balance After");

    balanceCol.setCellValueFactory(
        new PropertyValueFactory<>("balanceAfter")
    );

    TableColumn<Transaction, String> dateCol =
        new TableColumn<>("Date & Time");

    dateCol.setCellValueFactory(
        new PropertyValueFactory<>("dateTime")
    );

    table.getColumns().addAll(
        accountCol, typeCol, amountCol,
        balanceCol, dateCol
    );

    table.setItems(
        FXCollections.observableArrayList(
            FileManager.loadTransactions()
        )
    );

    Button backBtn = new Button("Back");

    backBtn.setOnAction(e -> historyStage.close());

    VBox layout = new VBox(
        20, heading, table, backBtn
    );

    layout.setAlignment(Pos.CENTER);
    layout.setPadding(new Insets(20));

    Scene scene = new Scene(layout, 850, 500);

    historyStage.setTitle(
        "Bank of Nitte - Transaction History"
    );

    historyStage.setScene(scene);
    historyStage.show();
}

private void showBalanceEnquiry(Stage stage) {

    Label heading = new Label("BALANCE ENQUIRY");

    heading.setStyle(
        "-fx-font-size: 24px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #1e3a8a;"
    );

    TextField accountField = new TextField();
    accountField.setPromptText("Enter account number");
    accountField.setMaxWidth(300);

    Button checkButton = new Button("Check Balance");
    Button backButton = new Button("Back");

    Label result = new Label();

    checkButton.setOnAction(e -> {

        String accountNumber =
            accountField.getText().trim();

        if (accountNumber.isEmpty()) {
            result.setText("Please enter account number.");
            return;
        }

        Account account =
            bankService.findAccount(accountNumber);

        if (account == null) {

            result.setText("Account not found!");

        } else {

            result.setText(
                "ACCOUNT DETAILS\n\n" +
                "Account Number: " +
                account.getAccountNumber() +
                "\nAccount Holder: " +
                account.getAccountHolderName() +
                "\nAvailable Balance: Rs. " +
                String.format("%.2f", account.getBalance())
            );
        }
    });

    backButton.setOnAction(e -> start(stage));

    VBox layout = new VBox(
        20, heading, accountField,
        checkButton, result, backButton
    );

    layout.setAlignment(Pos.CENTER);
    layout.setPadding(new Insets(30));

    layout.setStyle(
        "-fx-background-color: #eff6ff;"
    );

    Scene scene = new Scene(layout, 550, 450);

    stage.setTitle("Bank of Nitte - Balance Enquiry");
    stage.setScene(scene);
    stage.show();
}
private void showAllAccounts(Stage stage) {

    Label heading = new Label("ALL BANK ACCOUNTS");
    heading.setStyle(
        "-fx-font-size: 24px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #1e3a8a;"
    );

    TableView<Account> table = new TableView<>();

    TableColumn<Account, String> numberCol =
        new TableColumn<>("Account Number");

    numberCol.setCellValueFactory(data ->
        new javafx.beans.property.SimpleStringProperty(
            data.getValue().getAccountNumber()
        )
    );

    TableColumn<Account, String> nameCol =
        new TableColumn<>("Account Holder");

    nameCol.setCellValueFactory(data ->
        new javafx.beans.property.SimpleStringProperty(
            data.getValue().getAccountHolderName()
        )
    );

    TableColumn<Account, String> balanceCol =
        new TableColumn<>("Balance");

    balanceCol.setCellValueFactory(data ->
        new javafx.beans.property.SimpleStringProperty(
            String.format("%.2f", data.getValue().getBalance())
        )
    );

    table.getColumns().addAll(
        numberCol, nameCol, balanceCol
    );

    table.getItems().addAll(bankService.getAllAccounts());

    Button backButton = new Button("Back");
    backButton.setOnAction(e -> start(stage));

    VBox layout = new VBox(
        20, heading, table, backButton
    );

    layout.setAlignment(Pos.CENTER);
    layout.setPadding(new Insets(30));
    layout.setStyle("-fx-background-color: #eff6ff;");

    Scene scene = new Scene(layout, 650, 500);

    stage.setTitle("Bank of Nitte - All Accounts");
    stage.setScene(scene);
    stage.show();
}


    private void showMessage(String module) {

        Alert alert = new Alert(
            Alert.AlertType.INFORMATION
        );

        alert.setTitle("Smart Banking System");
        alert.setHeaderText(module);
        alert.setContentText(
            "This feature will be implemented soon."
        );

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
