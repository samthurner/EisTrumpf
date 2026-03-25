package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.audio.ButtonSoundManager;
import htl.steyr.demo.music.Music;
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
import htl.steyr.demo.audio.SoundUtil;
import javafx.scene.Parent;
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
    public Button prevSongButton;
    public Button nextSongButton;

    public void onSpielenBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("start-game-menu");
    }

    public void onKartenDecksBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("card-deck-manager-screen");
    }

    public void onStatsBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("stats-screen");
    }

    public void onSettingsBtnClicked(MouseEvent mouseEvent) {
        ButtonSoundManager.getInstance().playClick();
        ViewSwitcher.switchTo("settings-pane");
    }

    public void onEndGameBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.getStage().close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        volumeSlider.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                SoundUtil.applyButtonSound(newScene.getRoot());
            }
        });

        Timeline initialSoundCheck = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            if (volumeSlider.getScene() != null) {
                SoundUtil.applyButtonSound(volumeSlider.getScene().getRoot());
            }
        }));
        initialSoundCheck.play();


        Timeline waitForUserData = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    if (UserSession.getUserData() != null) {
                        usernameLabel.setText(UserSession.getUserData().getUsername());
                    }
                })
        );

        volumeSlider.setMin(0);
        volumeSlider.setMax(100);

        htl.steyr.demo.music.Music.startMusic();
        // Slider auf aktuelle Lautstärke setzen
        volumeSlider.setValue(Music.getVolume() * 100);

        // Listener für Lautstärke
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double volume = newVal.doubleValue() / 100.0;
            Music.setVolume(volume);
        });

        boolean visible = Music.isControlsVisible();
        prevSongButton.setVisible(visible);
        nextSongButton.setVisible(visible);


        waitForUserData.playFromStart();

    }


    public void toggleSliderButton(ActionEvent actionEvent) {
        if (volumeSlider.isVisible()) {
            volumeSlider.setVisible(false);
            prevSongButton.setVisible(false);
            nextSongButton.setVisible(false);
        }else {
            volumeSlider.setVisible(true);
            prevSongButton.setVisible(true);
            nextSongButton.setVisible(true);
        }
    }

    public void onNextSong(ActionEvent e) {
        Music.playNextRandom();
    }

    public void onPrevSong(ActionEvent e) {
        Music.playPrevious();
    }
}
