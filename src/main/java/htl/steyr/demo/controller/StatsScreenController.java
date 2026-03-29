package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.gameTimer.GameTimer;
import htl.steyr.demo.userdata.Statistik;
import htl.steyr.demo.userdata.UserData;
import htl.steyr.demo.userdata.UserSession;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class StatsScreenController implements Initializable {

    public Label gamesPlayedLabel;
    public Label gamesWonLabel;
    public Label winrateLabel;
    public Label winStreakLabel;
    public Button okButton;
    public Label gameTimeStatLabel;
    public UserData userData;
    private Statistik statistik;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userData = UserSession.getUserData();

        Timeline waitForUserData = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {

                    UserData user = UserSession.getUserData();
                    if (user == null) {
                        return;
                    }

                    statistik = new Statistik(userData, null);

                    gamesPlayedLabel.setText("gespielte Spiele: " + statistik.gamePlayedStat());
                    gamesWonLabel.setText("gewonnene Spiele: " + user.getGames_won());
                    winrateLabel.setText("Win-Rate: " + String.format("%.1f%%", statistik.winRateStat()));
                    winStreakLabel.setText("Längste Winstreak: " + user.getHighest_winstreak());
                    gameTimeStatLabel.setText("Gesamtspielzeit: " + GameTimer.formatToHHMMSS(user.getPlaytime()));
                })
        );

        waitForUserData.setCycleCount(Timeline.INDEFINITE);
        waitForUserData.play();
    }

    public void onOkClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("menu");
    }
}
