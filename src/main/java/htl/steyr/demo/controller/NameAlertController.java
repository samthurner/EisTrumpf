package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.userdata.UserData;
import htl.steyr.demo.userdata.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.File;

/**
 * Controller für das Dialogfenster zum Ändern des Benutzernamens.
 * Aktualisiert den Namen im UserData-Objekt und speichert ihn neu als JSON.
 */
public class NameAlertController {

    @FXML
    private TextField nameField; // Eingabefeld für den neuen Benutzernamen

    /**
     * Wird beim Klick auf "OK" ausgeführt.
     * Liest den neuen Namen aus, aktualisiert die UserData und löscht die alte JSON-Datei.
     */
    @FXML
    public void onOkayClicked() {
        String newName = nameField.getText(); // neuer eingegebener Benutzername

        if (newName != null && !newName.isBlank()) {
            UserData user = UserSession.getInstance().getUserData(); // aktueller Benutzer
            String oldName = user.getUsername(); // alter Benutzername

            user.setUsername(newName); // neuen Namen setzen
            user.writeToJson(); // neue JSON speichern

            File directory = new File("Json"); // Ordner mit User-Dateien
            for (File file : directory.listFiles()) {
                if (file.getName().equals(oldName + ".json")) {
                    file.delete(); // alte Datei löschen
                }
            }
        }

        ViewSwitcher.switchTo("settings-pane"); // zurück zu Einstellungen
    }

    /**
     * Wird beim Klick auf "Abbrechen" ausgeführt.
     * Kehrt ohne Änderungen zurück zu den Einstellungen.
     */
    @FXML
    public void onCancelClicked() {
        ViewSwitcher.switchTo("settings-pane");
    }
}