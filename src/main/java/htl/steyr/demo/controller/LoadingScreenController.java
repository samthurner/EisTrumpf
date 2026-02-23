package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.userdata.UserSession;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class LoadingScreenController implements Initializable {

    public Label loadingText;
    public Separator redLine;
    public ProgressIndicator loadingCircle;
    public ImageView logoImage;
    public Button exitButton;
    public Label ipAdressLabel;


    private final String[] texts = {
    };

    public void initialize() {
    }

    private String randomText() {
        return "";
    }

    public void onExit() throws IOException {
        UserSession.getGameServer().stop();
        ViewSwitcher.switchTo("start-game-menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ipAdressLabel.setText("Deine IP Adresse: " + Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            ipAdressLabel.setText("Deine IP Adresse konnte nicht gefunden werden.");
        }
    }
}
