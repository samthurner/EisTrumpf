package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.network.GameClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller für den Endscreen nach einem Spiel.
 * Zeigt an, ob der Spieler gewonnen oder verloren hat
 * und ermöglicht die Rückkehr ins Hauptmenü.
 */
public class EndScreenController implements Initializable {

    @FXML
    private VBox root; // Root-Container der Szene

    @FXML
    private Label winnerLabel; // Anzeige für Gewinner/Verlierer

    @FXML
    private Label infoLabel; // Zusätzlicher Informationstext

    /**
     * Setzt die Anzeige basierend auf dem Spielergebnis.
     *
     * @param playerWon true wenn der Spieler gewonnen hat, sonst false
     */
    public void setWinner(boolean playerWon) {
        winnerLabel.setText(playerWon ? "Gewinner!" : "Verlierer.");
        infoLabel.setText(playerWon ? "Du hast gewonnen!" : "Du hast verloren.");
    }

    /**
     * Wird beim Klick auf den Button ausgeführt.
     * Wechselt zurück ins Hauptmenü.
     */
    @FXML
    public void onBackToMenuClicked() {
        ViewSwitcher.switchTo("menu");
    }

    /**
     * Initialisiert den Screen und setzt das Spielergebnis.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setWinner(GameClient.isGameResult());
    }
}