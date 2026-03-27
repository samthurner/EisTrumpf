package htl.steyr.demo.network;

import com.google.gson.Gson;
import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.*;
import htl.steyr.demo.userdata.UserSession;
import javafx.application.Platform;

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

                String deckName = UserSession.getUserData().getSelectedDeck();
                Deck deckObj = null;

                Path userPath = Paths.get("Json/user/" + deckName);
                if(Files.exists(userPath)){
                    deckObj = DeckReader.loadDeck(Files.newInputStream(userPath));
                }else {
                    InputStream is = getClass().getResourceAsStream(
                            "/htl/steyr/demo/carddecks/" + deckName
                    );

                    if (is == null) {
                        throw new RuntimeException("Deck nicht gefunden: " + deckName);
                    }

                    deckObj = DeckReader.loadDeck(is);
                }

                deck = deckObj.getCards();

                initGame();

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
        if (clientHand == null || clientHand.isEmpty()) {
            send("game_result;loser");
            return;
        }

        PlayingCard card = clientHand.getFirst();

        opponent.send("send_card;" + new Gson().toJson(card));
        System.out.println("Sende Karte: " + card.getName());

        opponent.send("send_cards_left;" + clientHand.size());
        System.out.println("Verbleibende Karten des Clients: " + clientHand.size());
    }

    public void initGame() {
        hostHand.clear();
        clientHand.clear();

        Collections.shuffle(deck);

        int size = deck.size();
        for (int i = 0; i < size; i++) {
            if(i % 2 == 0){
                clientHand.add(deck.remove(0));
            }else{
                hostHand.add(deck.remove(0));
            }
        }
    }
}