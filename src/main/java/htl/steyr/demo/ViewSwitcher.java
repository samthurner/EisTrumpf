package htl.steyr.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ViewSwitcher {

    private static Stage stage;

    public static void setStage(Stage stage) {
        ViewSwitcher.stage = stage;
    }

    public static void switchTo(String fxmlFile) {
        try {
            String fxmlFilePath = "fxml/" + fxmlFile + ".fxml";
            Parent root = FXMLLoader.load(ViewSwitcher.class.getResource(fxmlFilePath));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
