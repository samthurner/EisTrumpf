package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.audio.ButtonSoundManager;
import htl.steyr.demo.audio.SoundUtil;
import htl.steyr.demo.music.Music;
import htl.steyr.demo.userdata.UserSession;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller für das Hauptmenü.
 * Steuert Navigation, Musik und UI-Interaktionen.
 */
public class MenuController implements Initializable {

    public Label usernameLabel;
    public Slider volumeSlider;
    public Button prevSongButton;
    public Button nextSongButton;

    /**
     * Öffnet das Spiel-Menü.
     */
    public void onSpielenBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("start-game-menu");
    }

    /**
     * Öffnet den Deck-Manager.
     */
    public void onKartenDecksBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("card-deck-manager-screen");
    }

    /**
     * Öffnet die Statistik.
     */
    public void onStatsBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("stats-screen");
    }

    /**
     * Öffnet die Einstellungen.
     */
    public void onSettingsBtnClicked(MouseEvent mouseEvent) {
        ButtonSoundManager.getInstance().playClick();
        ViewSwitcher.switchTo("settings-pane");
    }

    /**
     * Beendet die Anwendung.
     */
    public void onEndGameBtnClicked(ActionEvent actionEvent) {
        ViewSwitcher.getStage().close();
    }

    /**
     * Initialisiert Menü, Musik und Benutzeranzeige.
     */
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

        Music.startMusic();
        volumeSlider.setValue(Music.getVolume() * 100);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double volume = newVal.doubleValue() / 100.0;
            Music.setVolume(volume);
        });

        boolean visible = Music.isControlsVisible();
        prevSongButton.setVisible(visible);
        nextSongButton.setVisible(visible);

        waitForUserData.playFromStart();
    }

    /**
     * Zeigt oder versteckt Lautstärke- und Musiksteuerung.
     */
    public void toggleSliderButton(ActionEvent actionEvent) {
        if (volumeSlider.isVisible()) {
            volumeSlider.setVisible(false);
            prevSongButton.setVisible(false);
            nextSongButton.setVisible(false);
        } else {
            volumeSlider.setVisible(true);
            prevSongButton.setVisible(true);
            nextSongButton.setVisible(true);
        }
    }

    /**
     * Spielt den nächsten Song.
     */
    public void onNextSong(ActionEvent e) {
        Music.playNextRandom();
    }

    /**
     * Spielt den vorherigen Song.
     */
    public void onPrevSong(ActionEvent e) {
        Music.playPrevious();
    }
}