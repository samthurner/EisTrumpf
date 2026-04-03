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

/**
 * Controller für den Spielbildschirm.
 * Verwaltet Spielablauf, Kartenanzeige, Benutzeraktionen und Kommunikation
 * mit Client oder Server.
 */
public class GameScreenController implements Initializable {

    /** Singleton-Instanz für globalen Zugriff */
    private static GameScreenController instance;

    /**
     * Gibt die aktuelle Instanz zurück.
     *
     * @return GameScreenController Instanz
     */
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

    /** aktuell angezeigte Karte */
    private PlayingCard currentCard;

    /** Statistik-Tracker */
    private Statistik statistik;

    /** Benutzerdaten */
    private UserData userData;

    /** Client-Verbindung */
    private GameClient gameClient;

    /** Server-Verbindung */
    private GameServer gameServer;

    /**
     * Initialisiert Spiel, Timer, Benutzerdaten und UI.
     */
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

    /**
     * Setzt den GameServer.
     *
     * @param server Server-Instanz
     */
    public void setGameServer(GameServer server) {
        this.gameServer = server;
    }

    /**
     * Aktualisiert die angezeigte Karte und Statistiken.
     *
     * @param card aktuelle Karte
     * @param units Namen der Statistiken
     */
    public void updateCard(PlayingCard card, String[] units) {
        if (card == null) return;

        currentCard = card;
        cardNameLabel.setText(card.getName());
        stat1Button.setText(units[1] + ": " + card.getStat1());
        stat2Button.setText(units[2] + ": " + card.getStat2());
        stat3Button.setText(units[3] + ": " + card.getStat3());
        stat4Button.setText(units[4] + ": " + card.getStat4());
    }

    /**
     * Aktualisiert die Anzeige der verbleibenden Karten.
     *
     * @param cards Anzahl der Karten
     */
    public void updateCardLabel(int cards) {
        cardsLeftLabel.setText("Karten: " + cards);
    }

    /** Spieler ist am Zug */
    public void yourTurn() {
        turnLabel.setText("Du bist am Zug!");
        setDisableStatButtons(false);
    }

    /** Gegner ist am Zug */
    public void opponentTurn() {
        turnLabel.setText("Dein Gegner ist dran...");
        setDisableStatButtons(true);
    }

    /**
     * Zeigt das Ergebnis einer Runde an.
     *
     * @param text Ergebnistext
     */
    public void showRoundResult(String text) {
        turnLabel.setText(text);
        setDisableStatButtons(true);
    }

    /**
     * Zeigt an, welche Statistik der Gegner gewählt hat.
     *
     * @param statIndex Index der Statistik
     * @param units Namen der Statistiken
     */
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

    /**
     * Sendet die gewählte Statistik an Client oder Server.
     *
     * @param statIndex gewählte Statistik (1–4)
     */
    private void sendStatChoice(int statIndex) {
        setDisableStatButtons(true);

        if (gameClient != null) {
            gameClient.playStat(statIndex);
        } else if (gameServer != null) {
            gameServer.onHostStatChosen(statIndex);
        }
    }

    /**
     * Wird beim Aufgeben ausgelöst.
     * Stoppt Spiel und kehrt ins Menü zurück.
     */
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

    /**
     * Aktiviert oder deaktiviert alle Statistik-Buttons.
     *
     * @param disable true = deaktivieren
     */
    private void setDisableStatButtons(boolean disable) {
        stat1Button.setDisable(disable);
        stat2Button.setDisable(disable);
        stat3Button.setDisable(disable);
        stat4Button.setDisable(disable);
    }

    /**
     * Gibt den aktuellen GameTimer zurück.
     *
     * @return GameTimer oder null
     */
    public static GameTimer getGameTimer() {
        if (instance == null) {
            return null;
        }
        return instance.gameTimer;
    }
}