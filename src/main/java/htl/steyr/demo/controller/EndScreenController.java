package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.network.GameClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class EndScreenController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private Label winnerLabel;

    @FXML
    private Label infoLabel;

    public void setWinner(boolean playerWon) {
        winnerLabel.setText(playerWon ? "Gewinner!" : "Verlierer.");
        infoLabel.setText(playerWon ? "Du hast gewonnen!" : "Du hast verloren.");
    }

    @FXML
    public void onBackToMenuClicked() {
        ViewSwitcher.switchTo("menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setWinner(GameClient.isGameResult());
    }
}
