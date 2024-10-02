package pmproject.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onLoginButtonClick(ActionEvent event){
        System.out.printf("Username: %s\nPassword: %s",usernameField.getText(),passwordField.getText());
        //Create file manager instance
        FileManager fm = new FileManager();
        //Read the correct login information from the file
        Login correctLogin = fm.readFromLoginFile();
        //Create a Login object for the entered credentials
        Login enteredLogin = new Login(usernameField.getText(),passwordField.getText());
        if (correctLogin != null && enteredLogin.compareTo(correctLogin) == 0) {
            fm.successfulLogin();
            try {
                //Close the current login window and open the main window
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
                Scene mainScene = new Scene(fxmlLoader.load(), 650, 550);
                //Get the current stage (login window) and close it
                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();
                //Open the main window in a new stage
                Stage mainStage = new Stage();
                mainStage.setTitle("Password manager");
                mainStage.setScene(mainScene);
                mainStage.setMinHeight(550);
                mainStage.setMinWidth(650);
                mainStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            fm.failedLogin();
            welcomeText.setText("Wrong login or password");
        }
    }
    @FXML
    protected void onCreateAccountButtonClick(){
        System.out.println("Create account clicked");
        // Create a FileManager instance
        FileManager fm = new FileManager();
        // Check if the Login.txt file is empty
        Login existingLogin = fm.readFromLoginFile();
        // Get the text entered by the user in the username and password fields
        String enteredLogin = usernameField.getText();
        String enteredPassword = passwordField.getText();
        // Check if both username and password fields are not empty
        if (enteredLogin.isEmpty() || enteredPassword.isEmpty()) {
            welcomeText.setText("Both fields must be filled out");
        }
        else {
            // If the file is empty (i.e., no existing login), proceed with account creation
            if (existingLogin == null){
                // Write the entered username and password to the file
                fm.writeToLoginFile(enteredLogin,enteredPassword);
                welcomeText.setText("Account created successfully!");
            }
            else {
                welcomeText.setText("An account already exists.");
            }
        }

    }
}