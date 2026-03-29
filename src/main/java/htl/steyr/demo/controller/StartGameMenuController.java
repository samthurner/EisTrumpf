package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.network.GameClient;
import htl.steyr.demo.network.GameServer;
import htl.steyr.demo.userdata.UserSession;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class StartGameMenuController {

    public TextField hostPortField;
    public Button hostGoButton;
    public TextField joinIpField;
    public TextField joinPortField;
    public Button joinButton;
    public Button exitButton;

    public void onJoinButtonClicked(ActionEvent actionEvent) {
        String ip = joinIpField.getText();
        int port = Integer.parseInt(joinPortField.getText());

        GameClient client = new GameClient(ip, port);

        try {
            client.connect();
            UserSession.setClient(client);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Verbindung fehlgeschlagen.");
        }
    }

    public void onHostButtonClicked(ActionEvent actionEvent) {
        if (hostPortField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Fehler", "Bitte geben Sie einen Port ein.");
            return;
        }

        String selectedDeck = UserSession.getUserData().getSelectedDeck();
        if (selectedDeck == null || selectedDeck.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Kein Kartendeck ausgewählt", "Du musst zuerst ein Kartendeck auswählen.");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(hostPortField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Ungültige Eingabe", "Bitte geben Sie eine gültige Zahl ein.");
            return;
        }

        if (port < 1024 || port > 65534) {
            String text = port < 1024
                    ? "Der eingegebene Port muss größer als 1023 sein."
                    : "Der eingegebene Port muss kleiner als 65535 sein.";
            showAlert(Alert.AlertType.INFORMATION, "Wähle einen anderen Port", text);
            return;
        }

        GameServer server = new GameServer(port);
        try {
            server.start();
            UserSession.setHost(server);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Server konnte nicht gestartet werden.");
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
        ViewSwitcher.switchTo("menu");
    }

    public void enterOnIPLabel(ActionEvent actionEvent) {
        joinPortField.requestFocus();
    }
}