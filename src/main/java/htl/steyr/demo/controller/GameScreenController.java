package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.PlayingCard;
import htl.steyr.demo.gameTimer.GameTimer;
import htl.steyr.demo.network.GameClient;
import htl.steyr.demo.network.GameServer;
import htl.steyr.demo.userdata.Statistik;
import htl.steyr.demo.userdata.UserData;
import htl.steyr.demo.userdata.UserSession;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {

    private static GameScreenController instance;

    public static GameScreenController getInstance() {
        return instance;
    }

    public Button exitbtn;
    public Label cardsLeftLabel;
    public Label turnLabel;
    public VBox cardBox;
    public Label cardNameLabel;
    public Button stat1Button;
    public Button stat2Button;
    public Button stat3Button;
    public Button stat4Button;
    public Label gameTimeLabel;
    public GameTimer gameTimer;
    public Pane imagePlaceholder;
    public ImageView cardImage;

    private PlayingCard currentCard;
    private Statistik statistik;
    private UserData userData;
    private GameClient gameClient;
    private GameServer gameServer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;

        userData = UserSession.getUserData();

        gameTimer = new GameTimer(gameTimeLabel);
        gameTimer.start();

        statistik = new Statistik(userData, gameTimer);

        setDisableStatButtons(true);
        turnLabel.setText("Dein Gegner ist dran...");

        cardImage.setImage(new Image(getClass().getResource("/image/Schmidi.jpg").toExternalForm()));

        gameServer = UserSession.getGameServer();
        gameClient = UserSession.getGameClient();
    }

    public void setGameServer(GameServer server) {
        this.gameServer = server;
    }

    public void updateCard(PlayingCard card, String[] units) {
        if (card == null) return;
        currentCard = card;
        cardNameLabel.setText(card.getName());
        stat1Button.setText(units[1] + ": " + card.getStat1());
        stat2Button.setText(units[2] + ": " + card.getStat2());
        stat3Button.setText(units[3] + ": " + card.getStat3());
        stat4Button.setText(units[4] + ": " + card.getStat4());
    }

    public void updateCardLabel(int cards) {
        cardsLeftLabel.setText("Karten: " + cards);
    }

    public void yourTurn() {
        turnLabel.setText("Du bist am Zug!");
        setDisableStatButtons(false);
    }

    public void opponentTurn() {
        turnLabel.setText("Dein Gegner ist dran...");
        setDisableStatButtons(true);
    }

    public void showRoundResult(String text) {
        turnLabel.setText(text);
        setDisableStatButtons(true);
    }

    public void showOpponentChosenStat(int statIndex, String[] units) {
        if (units == null || statIndex >= units.length || units[statIndex] == null) return;
        turnLabel.setText("Gegner spielt: " + units[statIndex]);
    }

    public void onStat1Clicked(javafx.event.ActionEvent actionEvent) {
        if (currentCard == null) return;
        sendStatChoice(1);
    }

    public void onStat2Clicked(javafx.event.ActionEvent actionEvent) {
        if (currentCard == null) return;
        sendStatChoice(2);
    }

    public void onStat3Clicked(javafx.event.ActionEvent actionEvent) {
        if (currentCard == null) return;
        sendStatChoice(3);
    }

    public void onStat4Clicked(javafx.event.ActionEvent actionEvent) {
        if (currentCard == null) return;
        sendStatChoice(4);
    }

    private void sendStatChoice(int statIndex) {
        setDisableStatButtons(true);
        if (gameClient != null) {
            gameClient.playStat(statIndex);
        } else if (gameServer != null) {
            gameServer.onHostStatChosen(statIndex);
        }
    }

    public void onAufgebenClicked(javafx.event.ActionEvent actionEvent) {
        gameTimer.stop();
        statistik.gameTimeStat();
        if (gameClient != null) {
            try { gameClient.disconnect(); } catch (Exception ignored) {}
        } else if (gameServer != null) {
            try { gameServer.stop(); } catch (Exception ignored) {}
        }
        ViewSwitcher.switchTo("menu");
    }

    private void setDisableStatButtons(boolean disable) {
        stat1Button.setDisable(disable);
        stat2Button.setDisable(disable);
        stat3Button.setDisable(disable);
        stat4Button.setDisable(disable);
    }
}