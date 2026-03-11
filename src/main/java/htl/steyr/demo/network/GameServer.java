package htl.steyr.demo.network;

import com.google.gson.Gson;
import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.Deck;
import htl.steyr.demo.cards.DeckLoader;
import htl.steyr.demo.cards.DeckReader;
import htl.steyr.demo.cards.PlayingCard;
import javafx.application.Platform;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class GameServer {

    private int port;
    private boolean running;
    private ServerSocket serverSocket;
    private SocketConnection opponent;
    private Thread acceptThread;
    private List<PlayingCard> deck;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        running = true;
        serverSocket = new ServerSocket(port);
        System.out.println("Server wird gestartet auf Port " + port);

        acceptThread = new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();
                Platform.runLater(() -> ViewSwitcher.switchTo("game-screen"));

                System.out.println("Client verbunden!");

                Deck deckObj = DeckReader.loadDeck("src/resources/htl/steyr/demo/carddecks/auto_decks");

                deck = deckObj.getCards();

                Collections.shuffle(deck);

                opponent = new SocketConnection(socket);
                opponent.startListening();

                opponent.send("Testnachricht vom Server");

            } catch (IOException e) {
                if (running) e.printStackTrace();
            }
        });

        acceptThread.start();
    }

    public void send(String msg) {
        if (opponent != null) {
            opponent.send(msg);
        }
    }

    public void stop() throws IOException {
        System.out.println("Server wird gestoppt!");
        running = false;
        if (opponent != null) {
            opponent.close();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    private PlayingCard drawRandomCard(){
        int randomCardIndex = new Random().nextInt(deck.size());

        return deck.remove(randomCardIndex);
    }

    private void sendRandomCardToClient(){
        Gson gson = new Gson();

        PlayingCard card = drawRandomCard();

        String json  = gson.toJson(card);

        send("Card:" + json);
    }
}