package pmproject.passwordmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.animation.TranslateTransition;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainPageController {
    private Stage mainStage;
    @FXML
    private ScrollPane logsScrollPane;
    @FXML
    private ScrollPane accountsScrollPane;
    @FXML
    private HBox leftHBox;
    @FXML
    private VBox logsVBox;
    @FXML
    private VBox accountsVBox;
    @FXML
    private TextField logsSearchBar;
    @FXML
    private HBox bottomHBox;
    @FXML
    private Button logsButton;
    @FXML
    private Button addAccountButton;
    @FXML
    private Button generatePasswordButton;
    @FXML
    private Button showAccountsButton;
    @FXML
    private TextField accountSearchBar;
    @FXML
    private TextArea logsTextArea;
    @FXML
    private StackPane centerPane;
    @FXML
    private MenuItem loginMenuItem, accountsMenuItem, historyMenuItem, preferencesMenuItem;

    FileManager fm = new FileManager();
    private boolean logsVisible = false;
    private boolean accountsVisible = false;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    protected void initialize() {
        leftHBox.setSpacing(-logsScrollPane.getPrefWidth());

        // Add listener to accountSearchBar to handle search input
        accountSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterAccounts(newValue);
        });
        logsSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterLogs(newValue);
        });

    }

    @FXML
    protected void onAddAccountClicked() {
        // Logic for adding an account
        System.out.println("Add Account clicked");
        try {
            // Load the addaccount-view.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addaccount-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setTitle("Add new account");

            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(350);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onGeneratePasswordClicked() {
        // Logic for generating a password
        System.out.println("Generate Password clicked");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("genpsswd-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setTitle("Generate password");
            stage.setScene(scene);
            stage.setMinHeight(250);
            stage.setMinWidth(450);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onShowAccountsClicked() {
        // Logic for showing accounts
        System.out.println("Show Accounts clicked");
        accountsVisible = !accountsVisible;
        if (accountsVisible) {
            accountsScrollPane.setVisible(true);
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(300), accountsScrollPane);
            tt1.setFromX(accountsScrollPane.getPrefWidth());
            tt1.setToX(0);
            tt1.play();

            FileManager fileManager = new FileManager();
            List<Account> accounts = fileManager.readAccount();
            if (accounts.isEmpty()) {
                Label emptyMessage = new Label("You didn't add any account yet.");
                accountsVBox.getChildren().add(emptyMessage);
            } else {
                for (Account account : accounts) {
                    displayAccountDetails(account, fileManager);
                }
            }
        } else {
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(300), accountsScrollPane);
            tt1.setFromX(0);
            tt1.setToX(accountsScrollPane.getPrefWidth());
            tt1.play();
            tt1.setOnFinished(event -> {
                accountsScrollPane.setVisible(false);
                // Remove only dynamically added account details (leave the search bar)
                accountsVBox.getChildren().removeIf(node -> !(node instanceof TextField));
            });
        }
    }

    private void displayAccountDetails(Account account, FileManager fileManager) {
        VBox vb = new VBox(5);
        vb.setPadding(new Insets(6));

        // Add resource label and Edit button
        Label resourceLabel = new Label("Resource:    " + account.getResource());
        Button editResourceButton = new Button("Edit");
        editResourceButton.setOnAction(e -> onEditResourcePressed(resourceLabel, account.getResource(), fileManager));
        // Add delete button
        Button deleteAccountButton = new Button("Delete");
        deleteAccountButton.setOnAction(e -> onDeleteAccountPressed(account.getResource(), account.getLogin(), fileManager));
        // Add resource HBox (Label and Edit button)
        HBox resourceHBox = new HBox(5, resourceLabel, editResourceButton, deleteAccountButton);
        //Add login label and text. Add also copy and chage buttons
        Label loginLabel = new Label("Login:        ");
        TextField loginField = new TextField(account.getLogin());
        loginField.setPrefColumnCount(16);
        loginField.setEditable(false);
        Button copyLoginButton = new Button("Copy");
        Button changeLoginButton = new Button("Edit");
        // Add functionality to copy and change buttons for login
        copyLoginButton.setOnAction(e -> onCopyLoginPressed(loginField));
        changeLoginButton.setOnAction(e -> onChangeLoginPressed(loginField, account.getResource(), fileManager));

        HBox loginHBox = new HBox(3, loginLabel, loginField, copyLoginButton, changeLoginButton);

        // Password HBox
        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();
        passwordField.setText(account.getPassword());
        passwordField.setPrefColumnCount(16);
        passwordField.setEditable(false);

        TextField passwordTextField = new TextField();
        passwordTextField.setPrefColumnCount(16);
        passwordTextField.setVisible(false);
        passwordTextField.setManaged(false);

        Button showPasswordButton = new Button("Show");
        Button copyPasswordButton = new Button("Copy");
        Button changePasswordButton = new Button("Edit");

        HBox passwordHBox = new HBox(2, passwordLabel, passwordField, passwordTextField, showPasswordButton, copyPasswordButton, changePasswordButton);
        // Add functionality to password buttons
        showPasswordButton.setOnAction(e -> onShowPasswordPressed(passwordField, passwordTextField, showPasswordButton));
        copyPasswordButton.setOnAction(e -> onCopyPasswordPressed(passwordField));
        changePasswordButton.setOnAction(e -> onChangePasswordPressed(passwordField, account.getResource(), fileManager));
        // Add all elements to the VBox
        vb.getChildren().addAll(resourceHBox, loginHBox, passwordHBox);
        // Add the VBox to the accounts VBox in the ScrollPane
        accountsVBox.getChildren().add(vb);
    }

    private void onEditResourcePressed(Label resourceLabel, String oldResource, FileManager fileManager) {
        // Make the resource label editable (use TextField instead of Label)
        TextField resourceField = new TextField(oldResource);
        // Replace the label with the TextField in the HBox
        ((HBox) resourceLabel.getParent()).getChildren().set(0, resourceField);
        resourceField.requestFocus();
        // Save old value in case we need to compare
        String oldResourceName = resourceLabel.getText().substring(13);
        // Add event listener to detect when focus is lost (or enter is pressed)
        resourceField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                String newResourceName = resourceField.getText();
                if (!newResourceName.equals(oldResourceName)) {
                    // Update resource in FileManager
                    fileManager.updateResource(oldResourceName, newResourceName);
                    fileManager.theDataHasBeenChanged("Resource", newResourceName);
                    // Replace the TextField back with the updated label
                    resourceLabel.setText("Resource:    " + newResourceName);
                    ((HBox) resourceField.getParent()).getChildren().set(0, resourceLabel);
                }
            }
        });
    }

    private void onDeleteAccountPressed(String resourceName, String login, FileManager fileManager) {
        //Logic to delete the account
        fileManager.deleteAccount(resourceName);
        fileManager.theDataHasBeenChanged("Deleted", resourceName);
        // Remove only dynamically added account details (leave the search bar)
        accountsVBox.getChildren().removeIf(node -> !(node instanceof TextField));
        List<Account> updateAccounts = fileManager.readAccount();
        for (Account account : updateAccounts) {
            displayAccountDetails(account, fileManager);
        }
    }

    private void onCopyLoginPressed(TextField loginField) {
        // Logic to copy the login to the clipboard
        System.out.println("Copied login: " + loginField.getText());
        // Copy login text to clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(loginField.getText());
        clipboard.setContent(content);
    }

    private void onChangeLoginPressed(TextField loginField, String resourceName, FileManager fileManager) {
        // Logic to change the login
        System.out.println("Change login for resource: " + resourceName);
        // Make the login field editable
        loginField.setEditable(true);
        loginField.requestFocus();
        // Save the old value in case we need to compare changes later
        String oldLogin = loginField.getText();
        // Add an event listener to detect when the field loses focus (indicating user is done editing)
        loginField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) { // When the focus is lost
                String newLogin = loginField.getText();
                if (!newLogin.equals(oldLogin)) {
                    // Update the login in the file using FileManager
                    fileManager.updateLogin(resourceName, newLogin);
                    fileManager.theDataHasBeenChanged("Login", resourceName);
                    loginField.setEditable(false); // Make the field uneditable again
                }
            }
        });
    }

    private void onShowPasswordPressed(PasswordField passwordField, TextField passwordTextField, Button showPasswordButton) {
        // Logic to toggle password visibility
        if (passwordField.isVisible()) {
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);
            showPasswordButton.setText("Hide");
        } else {
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            showPasswordButton.setText("Show");
        }
    }

    private void onCopyPasswordPressed(PasswordField passwordField) {
        // Logic to copy the password to the clipboard
        System.out.println("Copied password: " + passwordField.getText());
        // Copy password text to clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(passwordField.getText());
        clipboard.setContent(content);
    }

    private void onChangePasswordPressed(PasswordField passwordField, String resourceName, FileManager fileManager) {
        // Logic to change the password
        System.out.println("Change password for resource: " + resourceName);
        // Make the password field editable
        passwordField.setEditable(true);
        passwordField.requestFocus();
        // Save the old password in case we need to compare changes later
        String oldPassword = passwordField.getText();
        // Add an event listener to detect when the field loses focus (indicating user is done editing)
        passwordField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) { // When the focus is lost
                String newPassword = passwordField.getText();
                if (!newPassword.equals(oldPassword)) {
                    // Update the password in the file using FileManager
                    fileManager.updatePassword(resourceName, newPassword);
                    fileManager.theDataHasBeenChanged("Password", resourceName);
                    passwordField.setEditable(false); // Make the field uneditable again
                }
            }
        });
    }

    @FXML
    protected void onLogsClicked() {
        // Logic for displaying logs
        System.out.println("Logs clicked");
        // Toggle visibility of logs pane
        logsVisible = !logsVisible;
        if (logsVisible) {
            FileManager fileManager = new FileManager();
            List<String> logs = fileManager.readHistory();
            logsTextArea.clear();
            // Combine logs into one single string with newlines separating each log
            StringBuilder logBuilder = new StringBuilder();
            for (String log : logs) {
                logBuilder.append(log).append("\n");
            }
            // Set the logs in the TextArea
            logsTextArea.setText(logBuilder.toString());

            logsScrollPane.setVisible(true);
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(300), logsScrollPane);
            tt1.setFromX(-logsScrollPane.getPrefWidth());
            tt1.setToX(0);
            TranslateTransition tt2 = new TranslateTransition(Duration.millis(300), logsButton);
            tt2.setFromX(0);
            tt2.setToX(150);
            tt1.play();
            tt2.play();

        } else {
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(300), logsScrollPane);
            tt1.setFromX(0);
            tt1.setToX(-logsScrollPane.getPrefWidth());
            TranslateTransition tt2 = new TranslateTransition(Duration.millis(300), logsButton);
            tt2.setFromX(logsScrollPane.getPrefWidth());
            tt2.setToX(0);
            tt1.play();
            tt2.play();
            tt1.setOnFinished(event -> {
                logsScrollPane.setVisible(false);
            });
        }
    }

    private void filterAccounts(String query) {
        FileManager fileManager = new FileManager();
        List<Account> allAccounts = fileManager.readAccount();
        // Filter accounts based on the search query using Stream API
        List<Account> filteredAccounts = allAccounts.stream().filter(account -> account.getResource().toLowerCase()
                .contains(query.toLowerCase())).collect(Collectors.toList());
        // Clear the dynamic account details from the VBox (preserve the search bar)
        accountsVBox.getChildren().removeIf(node -> !(node instanceof TextField));
        // If no matches, display "No matches" message
        if (filteredAccounts.isEmpty()) {
            Label noMatchesLabel = new Label("No matches");
            accountsVBox.getChildren().add(noMatchesLabel);
        } else {
            // Display the filtered accounts
            for (Account account : filteredAccounts) {
                displayAccountDetails(account, fileManager);
            }
        }
    }

    private void filterLogs(String query) {
        FileManager fileManager = new FileManager();
        List<String> allLogs = fileManager.readHistory();
        // Filter logs based on the search query using Stream API
        List<String> fileteredLogs = allLogs.stream().filter(log -> log.toLowerCase()
                .contains(query.toLowerCase())).collect(Collectors.toList());
        // Clear the TextArea before adding filtered logs
        logsTextArea.clear();
        // If no matches, display "No matches" message
        if (fileteredLogs.isEmpty()) {
            logsTextArea.setText("No matches");
        } else {
            // Display the filtered logs
            // Display the filtered logs in the TextArea
            StringBuilder logBuilder = new StringBuilder();
            for (String log : fileteredLogs) {
                logBuilder.append(log).append("\n");
            }
            logsTextArea.setText(logBuilder.toString());
        }
    }

    @FXML
    protected void onFileMenuItemClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("filedialog-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("File settings");
            dialogStage.initOwner(mainStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);

            dialogStage.setScene(scene);

            // Disable movement of the dialog window
            dialogStage.initStyle(javafx.stage.StageStyle.UNDECORATED); // Removes window decorations

            // Disable resizing of dialog
            dialogStage.setResizable(false);
            // Set the position after the dialog is shown to ensure scene dimensions are available
            dialogStage.setOnShown(event -> {
                double centerX = mainStage.getX() + (mainStage.getWidth() / 2) - (scene.getWidth() / 2);
                double centerY = mainStage.getY() + (mainStage.getHeight() / 2) - (scene.getHeight() / 2);
                dialogStage.setX(centerX);
                dialogStage.setY(centerY);
            });
            // Add listeners to main stage to update dialog position
            mainStage.xProperty().addListener((obs, oldVal, newVal) -> {
                dialogStage.setX(newVal.doubleValue() + (mainStage.getWidth() / 2) - (scene.getWidth() / 2));
            });
            mainStage.yProperty().addListener((obs, oldVal, newVal) -> {
                dialogStage.setY(newVal.doubleValue() + (mainStage.getHeight() / 2) - (scene.getHeight() / 2));
            });

            // Show the dialog and wait until it is closed before returning
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onAccountMenuItemClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("accountdialog-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onHistoryMenuItemClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("historydialog-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("History settings");
            dialogStage.initOwner(mainStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);

            dialogStage.setScene(scene);
            // Disable movement of the dialog window
            dialogStage.initStyle(javafx.stage.StageStyle.UNDECORATED); // Removes window decorations
            // Disable resizing of the main window
            dialogStage.setResizable(false);
            // Set the position after the dialog is shown to ensure scene dimensions are available
            dialogStage.setOnShown(event -> {
                double centerX = mainStage.getX() + (mainStage.getWidth() / 2) - (scene.getWidth() / 2);
                double centerY = mainStage.getY() + (mainStage.getHeight() / 2) - (scene.getHeight() / 2);
                dialogStage.setX(centerX);
                dialogStage.setY(centerY);
            });
            // Add listeners to main stage to update dialog position
            mainStage.xProperty().addListener((obs, oldVal, newVal ) -> {
                dialogStage.setX(newVal.doubleValue() + (mainStage.getWidth()/2) - (scene.getWidth()/2));
            });
            mainStage.yProperty().addListener((obs, oldVal, newVal) -> {
                dialogStage.setY(newVal.doubleValue() + (mainStage.getHeight()/2) - (scene.getHeight()/2));
            });
            // Show the dialog and wait until it is closed before returning
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
