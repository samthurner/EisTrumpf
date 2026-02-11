package htl.steyr.demo;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/htl/steyr/demo/fxml/login.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("EisTrumpf");
        stage.show();
    }
}
