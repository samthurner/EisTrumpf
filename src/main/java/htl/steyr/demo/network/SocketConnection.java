package htl.steyr.demo.network;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketConnection {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void send(String message) {
        out.println(message);
    }

    public void startListening() {
        Thread listenThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
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
}