package htl.steyr.demo.network;

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
        connection.startListening();

        connection.send("Hallo vom Client!");
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