package htl.steyr.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    private void onLoginClicked() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fehler");
            alert.setHeaderText("Bitte geben Sie einen Benutzernamen ein.");
            alert.showAndWait();
        }else{
            System.out.println("Spielername: " + username);
        }

    }
}
