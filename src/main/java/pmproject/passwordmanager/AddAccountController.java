package pmproject.passwordmanager;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddAccountController {
    @FXML
    private TextField resourceField;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisibleField;

    @FXML
    private Button showPasswordButton;
    @FXML
    private Button saveAccountButton;

    private boolean showPassword = false;

    @FXML
    protected void onShowPasswordClicked() {
        if (showPassword) {
            // Hide the plain text, show the masked text
            passwordVisibleField.setVisible(false);
            passwordField.setVisible(true);
            passwordField.requestFocus();
            passwordField.setText(passwordField.getText());
            showPasswordButton.setText("S");
        } else {
            // Show the plain text, hide the masked text
            passwordField.setVisible(false);
            passwordVisibleField.setVisible(true);
            passwordVisibleField.requestFocus();
            passwordVisibleField.setText(passwordField.getText());
            showPasswordButton.setText("H");
        }
        showPassword = !showPassword;
    }
    @FXML
    protected void onSaveAccountClicked () {
        FileManager fm = new FileManager();
        String resource = resourceField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();
        LocalDate date = LocalDate.now();
        if (!resource.isEmpty() || !login.isEmpty() || !password.isEmpty()) {
            fm.writeAccountToFile(resource, login, password, date);
            fm.accountAdded(resource);
            resourceField.clear();
            loginField.clear();
            passwordField.clear();
        }
        else {
            System.out.println("All fields are empty");
        }
    }
    @FXML
    protected void onCancelClicked() {
        // Logic for cancelling the operation (close window)
        Stage stage = (Stage) resourceField.getScene().getWindow();
        stage.close();
    }
}
