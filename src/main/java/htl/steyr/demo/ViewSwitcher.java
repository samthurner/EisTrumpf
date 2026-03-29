package htl.steyr.demo;

import htl.steyr.demo.audio.SoundUtil;
import htl.steyr.demo.userdata.UserSession;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.io.IOException;

public class ViewSwitcher {

    // Referenz auf die Hauptstage der Anwendung
    private static Stage stage;

    public static void setStage(Stage stage) {
        ViewSwitcher.stage = stage;
    }

    public static Stage getStage() {
        return ViewSwitcher.stage;
    }


    /**
     * Wechselt zu einer neuen FXML-View:
     * - lädt FXML
     * - setzt Stylesheet je nach Dark-/Lightmode
     * - aktiviert Button-Sounds
     * - passt Fenstergröße an Bildschirm an
     * - macht Fenster nicht skalierbar
     */
    public static void switchTo(String fxmlFile) {
        try {
            String fxmlFilePath = "fxml/" + fxmlFile + ".fxml";
            Parent root = FXMLLoader.load(ViewSwitcher.class.getResource(fxmlFilePath));

            Scene scene = new Scene(root);

            // Fügt Klick-Sound zu Buttons hinzu
            SoundUtil.applyButtonSound(root);
            // Theme global setzen
            scene.getStylesheets().clear();

            if (UserSession.getInstance().isDarkMode()) {
                scene.getStylesheets().add(
                        ViewSwitcher.class.getResource("/stylesheets/darkmode.css").toExternalForm()
                );
            } else {
                scene.getStylesheets().add(
                        ViewSwitcher.class.getResource("/stylesheets/whitemode.css").toExternalForm()
                );
            }

            // Scene setzen und anzeigen
            stage.setScene(scene);
            stage.show();

            // Fenster auf Bildschirmgröße setzen
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            // Fenstergröße fixieren
            stage.setResizable(false);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
