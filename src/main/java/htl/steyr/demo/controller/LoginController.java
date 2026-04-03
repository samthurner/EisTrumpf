package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.audio.ButtonSoundManager;
import htl.steyr.demo.userdata.UserData;
import htl.steyr.demo.userdata.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Controller für den Login-Screen.
 * Erstellt einen Benutzer und speichert ihn in der Session.
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    public UserData userData;

    /**
     * Wird beim Klick auf den Login-Button ausgeführt.
     * Überprüft Eingabe und erstellt UserData.
     */
    @FXML
    private void onLoginClicked() {

        ButtonSoundManager.getInstance().playClick();

        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fehler");
            alert.setHeaderText("Bitte geben Sie einen Benutzernamen ein.");
            alert.showAndWait();
        } else {
            userData = new UserData(username);
            UserSession.setUserData(userData);
            UserSession.getInstance().setDarkMode(userData.isDarkmode());

            ViewSwitcher.switchTo("menu");
        }
    }
}