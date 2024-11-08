package pmproject.passwordmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FileDialogController {
    @FXML
    private TextField currentPathFile;
    @FXML
    private TextField newPathFile;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private FileManager fileManager;

    public FileDialogController () {
        fileManager = new FileManager();
    }

    @FXML
    public void initialize () {

    }
    @FXML
    protected void onSaveButtonClicked () {

    }
    @FXML
    protected void onCancelButtonClicked (ActionEvent event) {
        Stage dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        dialogStage.close();
    }
}
