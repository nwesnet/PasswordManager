package pmproject.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;

    @FXML
    protected void onLoginButtonClick(ActionEvent event){
        System.out.printf("Username: %s\nPassword: %s",usernameField.getText(),passwordField.getText());
        //Close the current login window and open the main window
        FileManager fm = new FileManager();
        if (true) {
            fm.successfulLogin();
            try {
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
        }
    }
    @FXML
    protected void onCreateAccountButtonClick(){
        System.out.println("Create account clicked");
    }
}