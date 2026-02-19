package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class StartGameMenuController {

    public TextField hostPortField;
    public Button hostGoButton;
    public TextField joinIpField;
    public TextField joinPortField;
    public Button joinButton;
    public Button exitButton;

    public void onJoinButtonClicked(ActionEvent actionEvent) {

    }

    public void onHostButtonClicked(ActionEvent actionEvent) {

        if (hostPortField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Fehler", "Bitte geben Sie einen Port ein.");
            return;
        }

        int port;

        try {
            port = Integer.parseInt(hostPortField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Ungültige Eingabe", "Bitte geben Sie eine gültige Zahl ein.");
            return;
        }

        if (port < 1023 || port > 65534) {
            String text;

            if (port < 1023) {
                text = "Der eingegebene Port muss größer als 1023 sein.";
            } else {
                text = "Der eingegebene Port muss kleiner als 65535 sein.";
            }

            showAlert(Alert.AlertType.INFORMATION, "Wähle einen anderen Port", text);
            return;
        }

        ViewSwitcher.switchTo("loading-screen");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }


    public void onExitButtonClicked(ActionEvent actionEvent) {

    }
}