package htl.steyr.demo;


import htl.steyr.demo.music.Music;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ViewSwitcher.setStage(stage);

        Music music = new Music();
        music.playSong();

        Parent root = FXMLLoader.load(getClass().getResource("/htl/steyr/demo/fxml/login.fxml"));

        stage.setScene(new Scene(root));
        stage.setTitle("EisTrumpf");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
