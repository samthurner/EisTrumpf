package htl.steyr.demo.network;

import com.google.gson.Gson;
import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.PlayingCard;
import htl.steyr.demo.controller.GameScreenController;
import htl.steyr.demo.gameTimer.GameTimer;
import htl.steyr.demo.userdata.Statistik;
import htl.steyr.demo.userdata.UserData;
import htl.steyr.demo.userdata.UserSession;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;

/**
 * Client-Klasse für die Verbindung zu einem GameServer.
 * Verarbeitet eingehende Nachrichten und steuert das Spiel aus Client-Sicht.
 */
public class GameClient {

    private String ip; // Server-IP
    private int port; // Server-Port
    private SocketConnection connection; // Netzwerkverbindung
    private GameScreenController controller; // UI-Controller

    private static boolean gameResult = false; // Ergebnis des Spiels
    private static GameClient instance;

    private String[] units = new String[5]; // Namen der Statistiken

    /**
     * Konstruktor für den Client.
     *
     * @param ip Server-IP
     * @param port Server-Port
     */
    public GameClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        instance = this;
    }

    public static GameClient getInstance() {
        return instance;
    }

    public static boolean isGameResult() {
        return gameResult;
    }

    public static void setGameResult(boolean result) {
        GameClient.gameResult = result;
    }

    /**
     * Baut die Verbindung zum Server auf.
     *
     * @throws IOException bei Verbindungsfehlern
     */
    public void connect() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Mit Server verbunden!");

        connection = new SocketConnection(socket);
        connection.setGameClient(this);
        connection.startListening();

        Platform.runLater(() -> ViewSwitcher.switchTo("game-screen"));
    }

    /**
     * Verarbeitet eingehende Nachrichten vom Server.
     *
     * @param msg empfangene Nachricht
     */
    public void receivingMsg(String msg) {

        if (controller == null) {
            controller = GameScreenController.getInstance();
        }

        System.out.println("[CLIENT] Empfangen: " + msg);

        if (msg.startsWith("send_card;")) {
            String json = msg.substring("send_card;".length());
            PlayingCard card = new Gson().fromJson(json, PlayingCard.class);

            if (controller != null) {
                final String[] u = units;
                Platform.runLater(() -> controller.updateCard(card, u));
            }

        } else if (msg.startsWith("send_cards_left;")) {
            int cardsLeft = Integer.parseInt(msg.replace("send_cards_left;", "").trim());

            if (controller != null) {
                Platform.runLater(() -> controller.updateCardLabel(cardsLeft));
            }

        } else if (msg.startsWith("your_turn;")) {

            if (controller != null) {
                Platform.runLater(() -> controller.yourTurn());
            }

        } else if (msg.startsWith("play_stat;")) {
            int statIndex = Integer.parseInt(msg.replace("play_stat;", "").trim());

            if (controller != null) {
                Platform.runLater(() -> controller.showOpponentChosenStat(statIndex, units));
            }

        } else if (msg.startsWith("compare_result;")) {
            boolean iWon = Boolean.parseBoolean(msg.replace("compare_result;", "").trim());

            if (controller != null) {
                String text = iWon ? "Du hast gewonnen!" : "Gegner hat gewonnen.";
                Platform.runLater(() -> controller.showRoundResult(text));
            }

        } else if (msg.startsWith("game_result;")) {

            String result = msg.replace("game_result;", "").trim();
            boolean iWon = result.equals("winner");

            GameTimer gameTimer = GameScreenController.getGameTimer();
            UserData userData = UserSession.getUserData();
            Statistik statistik = new Statistik(userData, gameTimer);

            statistik.gameTimeStat();

            if (iWon) {
                statistik.gameWonStat();
            } else {
                statistik.gameLostStat();
            }

            setGameResult(iWon);

            Platform.runLater(() -> ViewSwitcher.switchTo("end-screen"));

        } else if (msg.startsWith("unit;")) {
            String[] parts = msg.split(";");
            int index = Integer.parseInt(parts[1]);
            units[index] = parts[2];

        } else if (msg.startsWith("start;")) {
            UserSession.setIsClient(true);
        }
    }

    /**
     * Sendet die gewählte Statistik an den Server.
     *
     * @param index Statistik-Index
     */
    public void playStat(int index) {
        send("play_stat;" + index);
    }

    /**
     * Sendet eine Nachricht an den Server.
     *
     * @param msg Nachricht
     */
    public void send(String msg) {
        if (connection != null) {
            connection.send(msg);
        }
    }

    /**
     * Trennt die Verbindung zum Server.
     *
     * @throws IOException bei Fehlern
     */
    public void disconnect() throws IOException {
        send("user_left;");
        if (connection != null) connection.close();
    }

    /**
     * Setzt den UI-Controller.
     *
     * @param controller GameScreenController
     */
    public void setController(GameScreenController controller) {
        this.controller = controller;
    }
}