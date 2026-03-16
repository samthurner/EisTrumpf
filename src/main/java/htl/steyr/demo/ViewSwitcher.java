package htl.steyr.demo;

import htl.steyr.demo.userdata.UserSession;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

            // THEME GLOBAL LADEN
            scene.getStylesheets().clear();
            if (UserSession.getInstance().isDarkMode()) {
                scene.getStylesheets().add(
                        ViewSwitcher.class.getResource("/stylesheets/darkmode.css").toExternalForm()
                );
            }else {
                scene.getStylesheets().add(
                        ViewSwitcher.class.getResource("/stylesheets/whitemode.css").toExternalForm()
                );
            }

//            stage.setFullScreen(true);
//            stage.setFullScreenExitHint("");
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

    public static void addToScene(String fxmlFile) {
        try {
            String fxmlFilePath = "fxml/" + fxmlFile + ".fxml";
            Parent newRoot = FXMLLoader.load(ViewSwitcher.class.getResource(fxmlFilePath));

            Parent currentRoot = stage.getScene().getRoot();

            if (currentRoot instanceof javafx.scene.layout.Pane pane) {
                pane.getChildren().add(newRoot);
            } else {
                System.out.println("Fehler");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
