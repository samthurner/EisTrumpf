package htl.steyr.demo.network;

import com.google.gson.Gson;
import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.*;
import htl.steyr.demo.userdata.UserSession;
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

    private List<PlayingCard> hostHand = new ArrayList<>();
    private List<PlayingCard> clientHand = new ArrayList<>();

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
                opponent = new SocketConnection(socket);
                opponent.startListening();

                Platform.runLater(() -> ViewSwitcher.switchTo("game-screen"));

                System.out.println("Client verbunden!");

                Deck deckObj = DeckReader.loadDeck("/htl/steyr/demo/carddecks/auto_deck.json");
                deck = deckObj.getCards();

                Collections.shuffle(deck);
                hostHand.add(deck.remove(0));

                sendCardToClient();
                sendYourTurn();

            } catch (IOException e) {
                if (running) e.printStackTrace();
            }
        });

        acceptThread.start();
    }

    public void sendCardsLeft(){

    }

    public void sendYourTurn() {
        send("your_turn;");

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

    private void sendCardToClient() {
        if (deck == null || deck.isEmpty()) {
            return;
        }

        PlayingCard card = deck.remove(0);
        clientHand.add(card);

        opponent.send("send_card;" + new Gson().toJson(card));
        System.out.println("Sende Karte: " + card.getName());

        // Bug 3 Fix: cardsLeft nach jeder gesendeten Karte mitschicken
        opponent.send("send_cards_left;" + deck.size());
        System.out.println("Verbleibende Karten: " + deck.size());
    }
}