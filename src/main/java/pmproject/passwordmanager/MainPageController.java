package pmproject.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.animation.TranslateTransition;
import javafx.stage.Stage;
import javafx.util.Duration;

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
