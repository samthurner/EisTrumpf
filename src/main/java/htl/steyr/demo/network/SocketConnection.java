package htl.steyr.demo.network;

import com.google.gson.Gson;
import htl.steyr.demo.userdata.UserData;
import htl.steyr.demo.userdata.UserSession;

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
    private UserData userData;

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.gson = new Gson();
        userData = UserSession.getUserData();
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
        String prefix = msg.split(";")[0];

        switch (prefix) {
            case "your_turn":
                break;
            case "card_request":
                break;
            case "get_cards":
                break;
            case "play_stat":
                break;
            case "set_username":
                break;
            case "send_card":
                break;
            case "game_result":
                break;
            case "compare_result":
                break;
            case "game_over":
                break;
            case "user_left":
                break;
            case "im_here":
                break;
        }

        //hier muss später die logik hin, um zu erkennen ob es ein Spielzug oder ein kartendeck oder so ist
    }
}