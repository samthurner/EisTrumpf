package htl.steyr.demo.network;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Verwaltet die Socket-Kommunikation zwischen Client und Server.
 * Sendet und empfängt Nachrichten.
 */
public class SocketConnection {

    private Socket socket; // Netzwerkverbindung
    private BufferedReader in; // Eingabestream
    private PrintWriter out; // Ausgabestream
    private Gson gson;

    private GameClient gameClient;
    private GameServer gameServer;

    /**
     * Konstruktor für die Verbindung.
     *
     * @param socket Socket-Verbindung
     * @throws IOException bei Fehlern
     */
    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.gson = new Gson();
    }

    /**
     * Sendet ein Objekt als JSON.
     *
     * @param obj Objekt
     */
    public void sendCard(Object obj) {
        out.println(gson.toJson(obj));
    }

    /**
     * Sendet eine Textnachricht.
     *
     * @param message Nachricht
     */
    public void send(String message) {
        out.println(message);
    }

    /**
     * Startet einen Thread zum Empfangen von Nachrichten.
     */
    public void startListening() {
        Thread listenThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    if (gameClient != null) gameClient.receivingMsg(msg);
                    if (gameServer != null) gameServer.receivingMsg(msg);
                }
            } catch (IOException e) {
                System.out.println("Verbindung beendet.");
            }
        });
        listenThread.setDaemon(true);
        listenThread.start();
    }

    /**
     * Schließt die Verbindung.
     *
     * @throws IOException bei Fehlern
     */
    public void close() throws IOException {
        socket.close();
    }

    public void setGameClient(GameClient client) {
        this.gameClient = client;
        this.gameServer = null;
    }

    public void setGameServer(GameServer server) {
        this.gameServer = server;
        this.gameClient = null;
    }
}