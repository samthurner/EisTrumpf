package htl.steyr.demo.network;

import com.google.gson.Gson;
import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.*;
import htl.steyr.demo.controller.GameScreenController;
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

public class GameServer {

    private int port;
    private boolean running;
    private ServerSocket serverSocket;
    private SocketConnection opponent;
    private Thread acceptThread;
    private static GameServer instance;

    private List<PlayingCard> deck;
    private List<String> units;
    private String[] unitsArray;

    private List<PlayingCard> hostHand = new ArrayList<>();
    private List<PlayingCard> clientHand = new ArrayList<>();

    private PlayingCard hostCurrentCard;
    private PlayingCard clientCurrentCard;

    private boolean hostTurn = true;

    private GameScreenController gameScreenController;

    public GameServer(int port) {
        this.port = port;
        instance = this;
    }

    public static GameServer getInstance() {
        return instance;
    }

    public void setGameScreenController(GameScreenController controller) {
        this.gameScreenController = controller;
    }

    public void start() throws IOException {
        running = true;
        serverSocket = new ServerSocket(port);
        System.out.println("Server gestartet auf Port " + port);

        acceptThread = new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();
                opponent = new SocketConnection(socket);
                opponent.setGameServer(this);
                opponent.startListening();

                System.out.println("Client verbunden!");

                String deckName = UserSession.getUserData().getSelectedDeck();
                Deck deckObj = loadDeck(deckName);

                if (deckObj == null) {
                    System.err.println("Deck konnte nicht geladen werden: " + deckName);
                    return;
                }

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

        System.err.println("Deck nicht gefunden: " + deckName);
        return null;
    }

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

        Timeline timeout = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            poll.stop();
            System.err.println("GameScreenController nie initialisiert.");
        }));
        timeout.play();

        poll.setOnFinished(null);
        Timeline stopWhenFound = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            if (gameScreenController != null) poll.stop();
        }));
        stopWhenFound.setCycleCount(Timeline.INDEFINITE);
        stopWhenFound.play();
    }

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

    public void onHostStatChosen(int statIndex) {
        send("play_stat;" + statIndex);
        resolveRound(statIndex);
    }

    private void onClientStatChosen(int statIndex) {
        Platform.runLater(() -> {
            if (gameScreenController != null) {
                gameScreenController.showOpponentChosenStat(statIndex, unitsArray);
            }
        });
        resolveRound(statIndex);
    }

    private void resolveRound(int statIndex) {
        PlayingCard winner = CardManager.compare(hostCurrentCard, clientCurrentCard, statIndex);
        boolean draw = (winner == null);
        boolean hostWon = !draw && winner == hostCurrentCard;

        if (draw) {
            hostHand.remove(0);
            hostHand.add(hostCurrentCard);
            clientHand.remove(0);
            clientHand.add(clientCurrentCard);
        } else if (hostWon) {
            hostHand.remove(0);
            hostHand.add(hostCurrentCard);
            PlayingCard won = clientHand.remove(0);
            hostHand.add(won);
            hostTurn = !hostTurn;
        } else {
            clientHand.remove(0);
            clientHand.add(clientCurrentCard);
            PlayingCard won = hostHand.remove(0);
            clientHand.add(won);
            hostTurn = !hostTurn;
        }

        send("compare_result;" + (!draw && !hostWon));

        String resultText = draw ? "Unentschieden!" : (hostWon ? "Du hast gewonnen!" : "Gegner hat gewonnen.");
        Platform.runLater(() -> {
            if (gameScreenController != null) gameScreenController.showRoundResult(resultText);
        });

        new Thread(() -> {
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
            Platform.runLater(this::startRound);
        }).start();
    }

    private void endGame(boolean hostWon) {
        send("game_result;" + (hostWon ? "loser" : "winner"));
        GameClient.setGameResult(hostWon);
        Platform.runLater(() -> ViewSwitcher.switchTo("end-screen"));
    }

    public void receivingMsg(String msg) {
        System.out.println("[SERVER] Empfangen: " + msg);
        if (msg.startsWith("play_stat;")) {
            int statIndex = Integer.parseInt(msg.replace("play_stat;", "").trim());
            Platform.runLater(() -> onClientStatChosen(statIndex));
        } else if (msg.equals("user_left;") || msg.equals("user_left")) {
            GameClient.setGameResult(true);
            Platform.runLater(() -> ViewSwitcher.switchTo("end-screen"));
        }
    }

    private void initGame() {
        hostHand.clear();
        clientHand.clear();
        Collections.shuffle(deck);
        int size = deck.size();
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                clientHand.add(deck.remove(0));
            } else {
                hostHand.add(deck.remove(0));
            }
        }
        System.out.println("Host: " + hostHand.size() + " Karten, Client: " + clientHand.size() + " Karten");
    }

    public void sendUnits() {
        for (int i = 0; i < units.size(); i++) {
            send("unit;" + (i + 1) + ";" + units.get(i));
        }
    }

    public void send(String msg) {
        if (opponent != null) opponent.send(msg);
    }

    public void stop() throws IOException {
        running = false;
        if (opponent != null) opponent.close();
        if (serverSocket != null) serverSocket.close();
    }
}