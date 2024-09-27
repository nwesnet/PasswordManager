package pmproject.passwordmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;

    @FXML
    protected void onLoginButtonClick(){
        System.out.printf("Username: %s\nPassword: %s",usernameField.getText(),passwordField.getText());
    }
    @FXML
    protected void onCreateAccountButtonClick(){
        System.out.println("Create account clicked");
    }
}