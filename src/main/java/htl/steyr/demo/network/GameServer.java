package htl.steyr.demo.network;

import java.io.IOException;
import java.net.ServerSocket;

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

        acceptThread = new Thread(() -> {

        });
        acceptThread.start();

    }

    public void stop() throws IOException {
        running = false;
        acceptThread.interrupt();
        serverSocket.close();
    }
}
