package htl.steyr.demo.network;

import com.google.gson.Gson;

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
    private GameClient gameClient;
    private GameServer gameServer;

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.gson = new Gson();
    }

    public void sendCard(Object obj) {
        out.println(gson.toJson(obj));
    }

    public void send(String message) {
        out.println(message);
    }

    public void startListening() {
        Thread listenThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    final String finalMsg = msg;
                    if (gameClient != null) {
                        gameClient.receivingMsg(finalMsg);
                    }
                    if (gameServer != null) {
                        gameServer.receivingMsg(finalMsg);
                    }
                    System.out.println("Empfangen: " + finalMsg);
                }
            } catch (IOException e) {
                System.out.println("Verbindung beendet.");
            }
        });
        listenThread.setDaemon(true);
        listenThread.start();
    }

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