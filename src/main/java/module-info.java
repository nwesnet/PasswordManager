module pmproject.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens pmproject.passwordmanager to javafx.fxml;
    exports pmproject.passwordmanager;
}