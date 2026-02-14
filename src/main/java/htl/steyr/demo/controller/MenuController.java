package htl.steyr.demo.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class MenuController {
    public Label usernameLabel;
    public Button btnSpielBeenden;
    public Button btnSpieled;
    public Button btnKarenDecks;
    public Button btnStatistik;
    public ImageView imgVolume;
    public ImageView imgSettings;

    public void onEndGameBtnClicked(ActionEvent actionEvent) {
        //das Spiel schließen
    }

    public void onSpielenBtnClicked(ActionEvent actionEvent) {
        //in die start-game-menu.fxml switchen
    }

    public void onKartenDecksBtnClicked(ActionEvent actionEvent) {
        //in die deck-explorer-screen.fxml switchen
    }

    public void onStatsBtnClicked(ActionEvent actionEvent) {
        //in die stats-screen.fxml switchen
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
        waitForUserData.playFromStart();
    }

}
