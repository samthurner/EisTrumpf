package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.userdata.UserSession;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    public Label usernameLabel;
    public Button btnSpielBeenden;
    public Button btnSpieled;
    public Button btnKarenDecks;
    public Button btnStatistik;
    public ImageView imgVolume;
    public ImageView imgSettings;
    public Slider volumeSlider;

    public void onSpielenBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("start-game-menu");
    }

    public void onKartenDecksBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("deck-explorer-screen");
    }

    public void onStatsBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("stats-screen");
    }

    public void onSettingsBtnClicked(MouseEvent mouseEvent) {ViewSwitcher.switchTo("settings-pane");
    }

    public void onEndGameBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.getStage().close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Timeline waitForUserData = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    if (UserSession.getUserData() != null) {
                        usernameLabel.setText(UserSession.getUserData().getUsername());
                    }
                })
        );

        volumeSlider.setMin(0);
        volumeSlider.setMax(100);

        // Slider auf aktuelle Lautstärke setzen
        volumeSlider.setValue(Music.getVolume() * 100);

        // Listener für Lautstärke
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double volume = newVal.doubleValue() / 100.0;
            Music.setVolume(volume);
        });



        waitForUserData.playFromStart();

    }


}
