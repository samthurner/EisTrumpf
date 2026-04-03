package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.network.AddressGetter;
import htl.steyr.demo.userdata.UserSession;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller für den Ladebildschirm.
 * Zeigt Ladeinformationen und die IP-Adresse an.
 */
public class LoadingScreenController implements Initializable {

    public Label loadingText;
    public Label ipAdressLabel;

    private final String[] texts = {};

    /**
     * Gibt einen zufälligen Text zurück (aktuell nicht implementiert).
     *
     * @return leerer String
     */
    private String randomText() {
        return "";
    }

    /**
     * Beendet den Server und kehrt zurück zum Startmenü.
     *
     * @throws IOException bei Fehlern
     */
    public void onExit() throws IOException {
        UserSession.getGameServer().stop();
        ViewSwitcher.switchTo("start-game-menu");
    }

    /**
     * Initialisiert den Screen und zeigt die IP-Adresse an.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ipAdressLabel.setText("Deine IP Adresse: " + AddressGetter.getIPAddress());
        } catch (Exception e) {
            ipAdressLabel.setText("Deine IP Adresse konnte nicht gefunden werden.");
        }
    }
}