package htl.steyr.demo.network;

import com.google.gson.Gson;
import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.*;
import htl.steyr.demo.controller.GameScreenController;
import htl.steyr.demo.gameTimer.GameTimer;
import htl.steyr.demo.userdata.Statistik;
import htl.steyr.demo.userdata.UserData;
import htl.steyr.demo.userdata.UserSession;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Server-Klasse für das Spiel.
 * Verwaltet Client-Verbindung, Spielablauf, Kartenverteilung und Rundenlogik.
 */
public class GameServer {

    private int port; // Port auf dem der Server läuft
    private boolean running; // Status des Servers
    private ServerSocket serverSocket; // Server-Socket
    private SocketConnection opponent; // Verbindung zum Client
    private Thread acceptThread; // Thread für eingehende Verbindungen

    private static GameServer instance;

    private List<PlayingCard> deck; // komplettes Kartendeck
    private List<String> units; // Namen der Statistiken
    private String[] unitsArray; // Statistik-Namen als Array

    private List<PlayingCard> hostHand = new ArrayList<>(); // Karten des Hosts
    private List<PlayingCard> clientHand = new ArrayList<>(); // Karten des Clients

    private PlayingCard hostCurrentCard; // aktuelle Karte des Hosts
    private PlayingCard clientCurrentCard; // aktuelle Karte des Clients

    private boolean hostTurn = true; // bestimmt wer am Zug ist

    private GameScreenController gameScreenController; // UI-Controller

    /**
     * Konstruktor für den Server.
     *
     * @param port Portnummer
     */
    public GameServer(int port) {
        this.port = port;
        instance = this;
    }

    public static GameServer getInstance() {
        return instance;
    }

    /**
     * Setzt den GameScreenController.
     *
     * @param controller UI-Controller
     */
    public void setGameScreenController(GameScreenController controller) {
        this.gameScreenController = controller;
    }

    /**
     * Startet den Server und wartet auf Client-Verbindungen.
     *
     * @throws IOException bei Fehlern
     */
    public void start() throws IOException {
        running = true;
        serverSocket = new ServerSocket(port);

        acceptThread = new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();

                opponent = new SocketConnection(socket);
                opponent.setGameServer(this);
                opponent.startListening();

                String deckName = UserSession.getUserData().getSelectedDeck();
                Deck deckObj = loadDeck(deckName);

                if (deckObj == null) return;

                deck = new ArrayList<>(deckObj.getCards());
                units = deckObj.getStatNames();

                unitsArray = new String[5];
                for (int i = 0; i < units.size(); i++) {
                    unitsArray[i + 1] = units.get(i);
                }

                send("start;");
                sendUnits();
                initGame();

                Platform.runLater(() -> {
                    ViewSwitcher.switchTo("game-screen");
                    waitForControllerThenStart();
                });

            } catch (IOException e) {
                if (running) e.printStackTrace();
            }
        });

        acceptThread.start();
    }

    /**
     * Lädt ein Deck aus Datei oder Ressourcen.
     *
     * @param deckName Name des Decks
     * @return Deck-Objekt oder null
     */
    private Deck loadDeck(String deckName) {
        if (deckName == null || deckName.isEmpty()) return null;

        Path userPath = Paths.get("json/decks/" + deckName);
        if (Files.exists(userPath)) {
            try {
                return DeckReader.loadDeck(Files.newInputStream(userPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream is = getClass().getResourceAsStream("/htl/steyr/demo/carddecks/" + deckName);
        if (is != null) {
            return DeckReader.loadDeck(is);
        }

        return null;
    }

    /**
     * Wartet bis der Controller geladen ist und startet dann das Spiel.
     */
    private void waitForControllerThenStart() {
        Timeline poll = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            GameScreenController ctrl = GameScreenController.getInstance();
            if (ctrl != null) {
                gameScreenController = ctrl;
                gameScreenController.setGameServer(this);
                startRound();
            }
        }));
        poll.setCycleCount(Timeline.INDEFINITE);
        poll.play();
    }

    /**
     * Startet eine neue Runde.
     */
    private void startRound() {

        if (hostHand.isEmpty()) {
            endGame(false);
            return;
        }

        if (clientHand.isEmpty()) {
            endGame(true);
            return;
        }

        hostCurrentCard = hostHand.get(0);
        clientCurrentCard = clientHand.get(0);

        send("send_card;" + new Gson().toJson(clientCurrentCard));
        send("send_cards_left;" + clientHand.size());

        if (gameScreenController != null) {
            gameScreenController.updateCard(hostCurrentCard, unitsArray);
            gameScreenController.updateCardLabel(hostHand.size());
        }

        if (hostTurn) {
            if (gameScreenController != null) gameScreenController.yourTurn();
        } else {
            send("your_turn;");
            if (gameScreenController != null) gameScreenController.opponentTurn();
        }
    }

    /**
     * Wird aufgerufen wenn der Host eine Statistik wählt.
     *
     * @param statIndex gewählte Statistik
     */
    public void onHostStatChosen(int statIndex) {
        send("play_stat;" + statIndex);
        resolveRound(statIndex);
    }

    /**
     * Wird aufgerufen wenn der Client eine Statistik wählt.
     *
     * @param statIndex gewählte Statistik
     */
    private void onClientStatChosen(int statIndex) {
        Platform.runLater(() -> {
            if (gameScreenController != null) {
                gameScreenController.showOpponentChosenStat(statIndex, unitsArray);
            }
        });
        resolveRound(statIndex);
    }

    /**
     * Vergleicht Karten und bestimmt den Gewinner der Runde.
     *
     * @param statIndex verwendete Statistik
     */
    private void resolveRound(int statIndex) {

        PlayingCard winner = CardManager.compare(hostCurrentCard, clientCurrentCard, statIndex);

        boolean draw = (winner == null);
        boolean hostWon = !draw && winner == hostCurrentCard;

        if (draw) {
            hostHand.add(hostHand.remove(0));
            clientHand.add(clientHand.remove(0));
        } else if (hostWon) {
            hostHand.add(hostHand.remove(0));
            hostHand.add(clientHand.remove(0));
            hostTurn = !hostTurn;
        } else {
            clientHand.add(clientHand.remove(0));
            clientHand.add(hostHand.remove(0));
            hostTurn = !hostTurn;
        }

        send("compare_result;" + (!draw && !hostWon));

        new Thread(() -> {
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
            Platform.runLater(this::startRound);
        }).start();
    }

    /**
     * Beendet das Spiel und speichert Statistiken.
     *
     * @param hostWon ob der Host gewonnen hat
     */
    private void endGame(boolean hostWon) {

        UserData userData = UserSession.getUserData();
        GameTimer gameTimer = GameScreenController.getGameTimer();

        Statistik statistik = new Statistik(userData, gameTimer);

        statistik.gameTimeStat();

        if (hostWon) statistik.gameWonStat();
        else statistik.gameLostStat();

        send("game_result;" + (hostWon ? "loser" : "winner"));

        GameClient.setGameResult(hostWon);

        Platform.runLater(() -> ViewSwitcher.switchTo("end-screen"));
    }

    /**
     * Verarbeitet Nachrichten vom Client.
     *
     * @param msg empfangene Nachricht
     */
    public void receivingMsg(String msg) {
        if (msg.startsWith("play_stat;")) {
            int statIndex = Integer.parseInt(msg.replace("play_stat;", "").trim());
            Platform.runLater(() -> onClientStatChosen(statIndex));
        }
    }

    /**
     * Initialisiert das Spiel und verteilt Karten.
     */
    private void initGame() {
        hostHand.clear();
        clientHand.clear();

        Collections.shuffle(deck);

        int size = deck.size();
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) clientHand.add(deck.remove(0));
            else hostHand.add(deck.remove(0));
        }
    }

    /**
     * Sendet alle Statistik-Namen an den Client.
     */
    public void sendUnits() {
        for (int i = 0; i < units.size(); i++) {
            send("unit;" + (i + 1) + ";" + units.get(i));
        }
    }

    /**
     * Sendet eine Nachricht an den Client.
     *
     * @param msg Nachricht
     */
    public void send(String msg) {
        if (opponent != null) opponent.send(msg);
    }

    /**
     * Stoppt den Server.
     *
     * @throws IOException bei Fehlern
     */
    public void stop() throws IOException {
        running = false;
        if (opponent != null) opponent.close();
        if (serverSocket != null) serverSocket.close();
    }
}