package htl.steyr.demo;


import htl.steyr.demo.music.Music;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ViewSwitcher.setStage(stage);

        Music music = new Music();
        music.playSong();

        Parent root = FXMLLoader.load(getClass().getResource("/htl/steyr/demo/fxml/login.fxml"));

        stage.setScene(new Scene(root));
        stage.setTitle("EisTrumpf");
        stage.initStyle(StageStyle.UNDECORATED);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        stage.show();

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
