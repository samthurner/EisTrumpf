package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EndScreenController {

    @FXML
    private VBox root;

    @FXML
    private Label winnerLabel;

    @FXML
    private Label infoLabel;

    public void setWinner(String winnerName, boolean playerWon) {
        winnerLabel.setText("Gewinner: " + winnerName);
        infoLabel.setText(playerWon ? "Du hast gewonnen!" : "Du hast verloren.");
    }

    @FXML
    public void onBackToMenuClicked() {
        ViewSwitcher.switchTo("menu");
    }
}
