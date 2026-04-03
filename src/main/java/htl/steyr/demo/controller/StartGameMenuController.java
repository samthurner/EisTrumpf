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

/**
 * Controller für das Start-Spiel-Menü.
 * Ermöglicht das Hosten oder Beitreten eines Spiels.
 */
public class StartGameMenuController {

    public TextField hostPortField; // Eingabe für Host-Port
    public Button hostGoButton;

    public TextField joinIpField; // Eingabe für Server-IP
    public TextField joinPortField; // Eingabe für Server-Port
    public Button joinButton;

    public Button exitButton;

    /**
     * Wird beim Klick auf "Join" ausgeführt.
     * Verbindet sich mit einem bestehenden Server.
     */
    public void onJoinButtonClicked(ActionEvent actionEvent) {
        String ip = joinIpField.getText(); // eingegebene IP
        int port = Integer.parseInt(joinPortField.getText()); // eingegebener Port

        GameClient client = new GameClient(ip, port);

        try {
            client.connect(); // Verbindung herstellen
            UserSession.setClient(client);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Verbindung fehlgeschlagen.");
        }
    }

    /**
     * Wird beim Klick auf "Host" ausgeführt.
     * Startet einen neuen Server.
     */
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
            port = Integer.parseInt(hostPortField.getText()); // Port parsen
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
            server.start(); // Server starten
            UserSession.setHost(server);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Server konnte nicht gestartet werden.");
            return;
        }

        ViewSwitcher.switchTo("loading-screen");
    }

    /**
     * Zeigt ein Alert-Fenster an.
     *
     * @param type Typ des Alerts
     * @param title Titel des Fensters
     * @param message Nachricht
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Kehrt zurück ins Menü.
     */
    public void onExitButtonClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("menu");
    }

    /**
     * Setzt den Fokus automatisch auf das Port-Feld nach IP-Eingabe.
     */
    public void enterOnIPLabel(ActionEvent actionEvent) {
        joinPortField.requestFocus();
    }
}