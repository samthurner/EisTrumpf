package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.Deck;
import htl.steyr.demo.cards.DeckLoader;
import htl.steyr.demo.cards.PlayingCard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {

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

    private Deck deck;
    private PlayingCard currentCard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Timeline waitForDeck = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {

                    if (deck == null) {
                        deck = DeckLoader.getLoadedDeck();
                        if (deck == null) return;

                        updateCard();
                    }
                })
        );

        waitForDeck.setCycleCount(Timeline.INDEFINITE);
        waitForDeck.play();
    }

    private void updateCard() {
        if (deck.getCards().isEmpty()) {
            cardNameLabel.setText("Keine Karten mehr");
            stat1Button.setText("");
            stat2Button.setText("");
            stat3Button.setText("");
            stat4Button.setText("");
            cardsLeftLabel.setText("Cards: 0");
            return;
        }

        currentCard = deck.getCards().get(0);

        cardNameLabel.setText(currentCard.getName());

        stat1Button.setText(deck.getStatNames().get(0) + ": " + currentCard.getStat1());
        stat2Button.setText(deck.getStatNames().get(1) + ": " + currentCard.getStat2());
        stat3Button.setText(deck.getStatNames().get(2) + ": " + currentCard.getStat3());
        stat4Button.setText(deck.getStatNames().get(3) + ": " + currentCard.getStat4());

        cardsLeftLabel.setText("Cards: " + deck.getCards().size());
    }

    public void onAufgebenClicked(javafx.event.ActionEvent actionEvent) {
        ViewSwitcher.switchTo("menu");
    }

    public void onStat1Clicked(javafx.event.ActionEvent actionEvent) {
        sendStatChoice(1, currentCard.getStat1());
    }

    public void onStat2Clicked(javafx.event.ActionEvent actionEvent) {
        sendStatChoice(2, currentCard.getStat2());
    }

    public void onStat3Clicked(javafx.event.ActionEvent actionEvent) {
        sendStatChoice(3, currentCard.getStat3());
    }

    public void onStat4Clicked(javafx.event.ActionEvent actionEvent) {
        sendStatChoice(4, currentCard.getStat4());
    }

    private void sendStatChoice(int statIndex, double value) {
        System.out.println("Spieler hat Stat " + statIndex + " gewählt mit Wert: " + value);

        // HIER später Online-Logik einbauen
        // z.B. Server.sendStatChoice(statIndex, value);
    }
}
