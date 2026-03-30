package htl.steyr.demo.audio;

import javafx.scene.Parent;
import javafx.scene.control.Button;

public class SoundUtil {

    public static void applyButtonSound(Parent root) {
        if (root == null) return;

        // Sucht alle Buttons in der FXML
        root.lookupAll(".button").forEach(node -> {
            Button button = (Button) node;


            button.setOnMousePressed(e -> {
                ButtonSoundManager.getInstance().playClick();
            });
        });
    }
}