package htl.steyr.demo.network;

import com.google.gson.Gson;
import htl.steyr.demo.cards.Deck;
import htl.steyr.demo.cards.DeckLoader;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketConnection {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson;

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.gson = new Gson();
    }

    // Verschickt elemente als Json objekt
    public void sendMessage(Object obj) {
        String Json = gson.toJson(obj);
        out.println(Json);
    }

    public void send(String message) {
        out.println(message);
    }

    public void startListening() {
        Thread listenThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    handleMessage(msg);
                    System.out.println("Empfangen: " + msg);
                }
            } catch (IOException e) {
                System.out.println("Verbindung beendet.");
            }
        });
        listenThread.start();
    }

    public void close() throws IOException {
        socket.close();
    }

    private void handleMessage(String msg) {
        if(msg.startsWith("{") && msg.contains("deck_name")) {
            Gson gson = new Gson();
            Deck receivedDeck = gson.fromJson(msg, Deck.class);

            DeckLoader.setLoadedDeck(receivedDeck);
            System.out.println("Deck empfangen" +  receivedDeck.getDeckName());

            javafx.application.Platform.runLater(() -> {
                htl.steyr.demo.ViewSwitcher.switchTo("game-screen");
            });        }



        System.out.println("Nachricht empfangen: ");
        //hier muss später die logik hin, um zu erkennen ob es ein Spielzug oder ein kartendeck oder so ist
    }
}