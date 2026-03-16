package htl.steyr.demo;

import htl.steyr.demo.userdata.UserSession;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

public class ViewSwitcher {

    private static Stage stage;

    public static void setStage(Stage stage) {
        ViewSwitcher.stage = stage;
    }

    public static Stage getStage() {
        return ViewSwitcher.stage;
    }

    public static void switchTo(String fxmlFile) {
        try {
            String fxmlFilePath = "fxml/" + fxmlFile + ".fxml";
            Parent root = FXMLLoader.load(ViewSwitcher.class.getResource(fxmlFilePath));
            Scene scene = new Scene(root);

            applyTheme(scene);

            //stage.setFullScreen(true);
            //stage.setFullScreenExitHint("");
            stage.setScene(scene);

            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hilfsmethode, damit ich den Code nicht doppelt schreiben muss
    private static void applyTheme(Scene scene) {
        if (scene == null) return;

        scene.getStylesheets().clear();

        // Wir prüfen, ob ein User da ist UND ob er Darkmode will
        if (UserSession.getUserData() != null && UserSession.getInstance().isDarkMode()) {
            try {
                String cssPath = ViewSwitcher.class.getResource("/stylesheets/darkmode.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
            }catch (NullPointerException e){
                System.out.println("CSS-Datei nicht gefunden!");
            }

        }
    }

    public static void addToScene(String fxmlFile) {
        try {
            String fxmlFilePath = "fxml/" + fxmlFile + ".fxml";
            Parent newRoot = FXMLLoader.load(ViewSwitcher.class.getResource(fxmlFilePath));

            // Wir holen das aktuelle Layout und sagen Java: "Das ist ein Pane" (einfaches Casting)
            // Ein Pane ist einfach ein Behälter für andere Elemente.
            Pane currentPane = (Pane) stage.getScene().getRoot();

            // Neues Element hinzufügen
            currentPane.getChildren().add(newRoot);

            // Theme aktualisieren, damit auch das neue Element dunkel ist
            applyTheme(stage.getScene());

        } catch (Exception e) {
            System.out.println("Fehler beim Hinzufügen zur Scene. Checke das Layout!");
            e.printStackTrace();
        }
    }
}
