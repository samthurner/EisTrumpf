package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.userdata.UserData;
import htl.steyr.demo.userdata.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

/**
 * Controller für die Einstellungen.
 * Verwaltet Theme, Benutzername und Statistik-Reset.
 */
public class SettingsController {

    @FXML
    private ToggleButton changeTheme; // Schalter für Dark-/Light-Mode

    /**
     * Wird beim Ändern des Themes ausgelöst.
     * Speichert die Einstellung und passt das Stylesheet an.
     *
     * @param actionEvent Event des Buttons
     */
    @FXML
    public void onThemechange(ActionEvent actionEvent) {
        boolean dark = changeTheme.isSelected(); // aktueller Zustand des Schalters

        UserSession.getInstance().setDarkMode(dark);

        UserData user = UserSession.getUserData();
        if (user != null) {
            user.setDarkmode(dark); // Theme im User speichern
            user.writeToJson(); // speichern
        }

        var scene = changeTheme.getScene(); // aktuelle Szene
        scene.getStylesheets().clear();

        if (dark) {
            scene.getStylesheets().add(getClass().getResource("/stylesheets/darkmode.css").toExternalForm());
        } else {
            scene.getStylesheets().add(getClass().getResource("/stylesheets/whitemode.css").toExternalForm());
        }
    }

    /**
     * Öffnet den Dialog zum Ändern des Namens.
     */
    @FXML
    public void onChangeNameClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("name-alert");
    }

    /**
     * Setzt alle Statistiken des Benutzers zurück.
     */
    @FXML
    public void onResetStatsClicked(ActionEvent actionEvent) {
        UserData user = UserSession.getInstance().getUserData();
        user.resetStats(); // Statistik zurücksetzen
        user.writeToJson(); // speichern
        System.out.println("Stats wurden zurückgesetzt.");
    }

    /**
     * Kehrt zurück zum Hauptmenü.
     */
    @FXML
    public void onBackClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("menu");
    }

    /**
     * Initialisiert den Screen und setzt den Theme-Schalter
     * basierend auf den gespeicherten User-Daten.
     */
    @FXML
    public void initialize() {
        if (UserSession.getUserData() != null) {
            changeTheme.setSelected(UserSession.getUserData().isDarkmode());
        }
    }
}