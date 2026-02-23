package htl.steyr.demo.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    private int port;
    private boolean running;
    private ServerSocket serverSocket;
    private SocketConnection opponent;
    private Thread acceptThread;

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
                System.out.println("Client verbunden!");

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
}