package pmproject.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.animation.TranslateTransition;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class MainPageController {
    @FXML
    private ScrollPane logsScrollPane;
    @FXML
    private ScrollPane accountsScrollPane;
    @FXML
    private HBox leftHBox;
    @FXML
    private VBox accountsVBox;
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
    private TextArea logsTextArea;

    private boolean logsVisible = false;
    private boolean accountsVisible = false;

    @FXML
    protected void initialize () {
        leftHBox.setSpacing(-logsScrollPane.getPrefWidth());
    }

    @FXML
    protected void onAddAccountClicked(){
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
        if(accountsVisible){
            accountsScrollPane.setVisible(true);
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(300),accountsScrollPane);
            tt1.setFromX(accountsScrollPane.getPrefWidth());
            tt1.setToX(0);
            tt1.play();

            FileManager fileManager = new FileManager();
            List<Account> accounts = fileManager.readAccount();
            if (accounts.isEmpty()) {
                Label emptyMessage = new Label("You didn't add any account yet.");
                accountsVBox.getChildren().add(emptyMessage);
            }
            else {
                for (Account account: accounts){
                    displayAccountDetails(account,fileManager);
                }
            }
        }
        else {
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(300),accountsScrollPane);
            tt1.setFromX(0);
            tt1.setToX(accountsScrollPane.getPrefWidth());
            tt1.play();
            tt1.setOnFinished(event -> {
                accountsScrollPane.setVisible(false);
            });
        }
    }
    private void displayAccountDetails(Account account, FileManager fileManager){
        VBox vb = new VBox(5);
        vb.setPadding(new Insets(6));

        Label resourceLabel = new Label("Resource:    " + account.getResource());

        Label loginLabel = new Label("Login:       ");
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
        vb.getChildren().addAll(resourceLabel, loginHBox, passwordHBox);

        // Add the VBox to the accounts VBox in the ScrollPane
        accountsVBox.getChildren().add(vb);
    }
    private void onCopyLoginPressed(TextField loginField) {
        // Logic to copy the login to the clipboard
        System.out.println("Copied login: " + loginField.getText());
    }
    private void onChangeLoginPressed(TextField loginField, String resourceName, FileManager fileManager) {
        // Logic to change the login
        System.out.println("Change login for resource: " + resourceName);
    }
    private void onShowPasswordPressed(PasswordField passwordField, TextField passwordTextField, Button showPasswordButton) {
        // Logic to toggle password visibility
        if (passwordField.isVisible()) {
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordTextField.setVisible(true);
            showPasswordButton.setText("Hide Password");
        } else {
            passwordField.setVisible(true);
            passwordTextField.setVisible(false);
            showPasswordButton.setText("Show Password");
        }
    }
    private void onCopyPasswordPressed(PasswordField passwordField) {
        // Logic to copy the password to the clipboard
        System.out.println("Copied password: " + passwordField.getText());
    }
    private void onChangePasswordPressed(PasswordField passwordField, String resourceName, FileManager fileManager) {
        // Logic to change the password
        System.out.println("Change password for resource: " + resourceName);
    }

    @FXML
    protected void onLogsClicked() {
        // Logic for displaying logs
        System.out.println("Logs clicked");
        System.out.println(logsVisible);
        // Toggle visibility of logs pane
        logsVisible = !logsVisible;
        if(logsVisible){
            logsScrollPane.setVisible(true);
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(300),logsScrollPane);
            tt1.setFromX(-logsScrollPane.getPrefWidth());
            tt1.setToX(0);
            TranslateTransition tt2 = new TranslateTransition(Duration.millis(300),logsButton);
            tt2.setFromX(0);
            tt2.setToX(150);
            tt1.play();
            tt2.play();

        }
        else {
            TranslateTransition tt1 = new TranslateTransition(Duration.millis(300),logsScrollPane);
            tt1.setFromX(0);
            tt1.setToX(-logsScrollPane.getPrefWidth());
            TranslateTransition tt2 = new TranslateTransition(Duration.millis(300),logsButton);
            tt2.setFromX(logsScrollPane.getPrefWidth());
            tt2.setToX(0);
            tt1.play();
            tt2.play();
            tt1.setOnFinished(event -> {
                logsScrollPane.setVisible(false);
            });
        }
    }
}
