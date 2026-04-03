package htl.steyr.demo.audio;

import javafx.scene.Parent;
import javafx.scene.control.Button;

public class SoundUtil {

    // Fügt allen Buttons im übergebenen UI-Root einen Klick-Sound hinzu
    public static void applyButtonSound(Parent root) {
        // root = Wurzelknoten der Szene (z.B. aus FXML)
        if (root == null) return;

        // Sucht alle Nodes mit der CSS-Klasse ".button"
        root.lookupAll(".button").forEach(node -> {
            // Cast zu Button, da lookupAll allgemeine Nodes liefert
            Button button = (Button) node;

            // Setzt Event: beim Drücken wird der Sound abgespielt
            button.setOnMousePressed(e -> {
                ButtonSoundManager.getInstance().playClick();
            });
        });
    }
}