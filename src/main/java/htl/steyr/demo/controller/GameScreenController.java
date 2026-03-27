package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.Deck;
import htl.steyr.demo.cards.DeckLoader;
import htl.steyr.demo.cards.PlayingCard;
import htl.steyr.demo.gameTimer.GameTimer;
import htl.steyr.demo.network.GameClient;
import htl.steyr.demo.network.GameServer;
import htl.steyr.demo.userdata.Statistik;
import htl.steyr.demo.userdata.UserData;
import htl.steyr.demo.userdata.UserSession;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
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

    private Timeline waitForDeck;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;

        userData = UserSession.getUserData();

        gameTimer = new GameTimer(gameTimeLabel);
        gameTimer.start();

        statistik = new Statistik(userData, gameTimer);

        turnLabel.setText("Dein gegner ist dran");

        cardImage.setImage(new Image(getClass().getResource("/image/Schmidi.jpg").toExternalForm()));
        if(UserSession.isClient()){
            gameClient = GameClient.getInstance();
        }else{
            gameServer = GameServer.getInstance();
        }

    }


    public void updateCard(PlayingCard card, String[] units) {
        if (card != null) {
            currentCard = card;
            cardNameLabel.setText(card.getName());
            stat1Button.setText(units[1] + ": " + card.getStat1());
            stat2Button.setText(units[2] + ": " + card.getStat2());
            stat3Button.setText(units[3] + ": " + card.getStat3());
            stat4Button.setText(units[4] + ": " + card.getStat4());
        }
    }
    public void updateCardLabel(int cards){
        cardsLeftLabel.setText("Cards: " + cards);
    }

    public void yourTurn() {
        Platform.runLater(() -> {
            turnLabel.setText("Du bist am Zug");
        });
    }

    public void onAufgebenClicked(javafx.event.ActionEvent actionEvent) {
        gameTimer.stop();
        statistik.gameTimeStat();
        ViewSwitcher.switchTo("menu");
    }

    public void onStat1Clicked(javafx.event.ActionEvent actionEvent) {
        if (currentCard == null) return;
        sendStatChoice(1, currentCard.getStat1());
    }

    public void onStat2Clicked(javafx.event.ActionEvent actionEvent) {
        if (currentCard == null) return;
        sendStatChoice(2, currentCard.getStat2());
    }

    public void onStat3Clicked(javafx.event.ActionEvent actionEvent) {
        if (currentCard == null) return;
        sendStatChoice(3, currentCard.getStat3());
    }

    public void onStat4Clicked(javafx.event.ActionEvent actionEvent) {
        if (currentCard == null) return;
        sendStatChoice(4, currentCard.getStat4());
    }

    private void sendStatChoice(int statIndex, int value) {
        System.out.println("Spieler hat Stat " + statIndex + " gewählt mit Wert: " + value);
        if(gameServer == null && gameClient != null){
            gameClient.playStat(statIndex);
        }
    }
}