package htl.steyr.demo.network;

import com.google.gson.Gson;
import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.PlayingCard;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;

public class GameClient {

    private String ip;
    private int port;
    private SocketConnection connection;

    public GameClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Mit Server verbunden!");

        connection = new SocketConnection(socket);
        connection.setGameClient(this);
        connection.startListening();

        Platform.runLater(() -> ViewSwitcher.switchTo("game-screen"));
    }

    public void receivingMsg(String msg) {
        if (msg.startsWith("send_card;")){
            String json = msg.substring("send_card;".length());

            Gson gson = new Gson();

            PlayingCard card = gson.fromJson(json, PlayingCard.class);

            GameSession.addCard(card);

            System.out.println("Karte erhalten: " + card.getName());
        }
    }

    public void send(String msg) {
        if (connection != null) {
            connection.send(msg);
        }
    }

    public void disconnect() throws IOException {
        if (connection != null) connection.close();
    }
}