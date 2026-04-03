package htl.steyr.demo.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Hilfsklasse zum Ermitteln der eigenen IP-Adresse.
 * Baut eine Verbindung zu einem externen Server auf,
 * um die lokale Netzwerkadresse zu bestimmen.
 */
public class AddressGetter {

    /**
     * Ermittelt die lokale IP-Adresse des Geräts.
     * Nutzt dafür eine Socket-Verbindung zu google.com.
     *
     * @return IP-Adresse als String
     */
    public static String getIPAddress() {
        String ipAddress = ""; // gespeicherte IP-Adresse

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80)); // Verbindung herstellen
            ipAddress = socket.getLocalAddress().getHostAddress(); // lokale IP auslesen
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ipAddress;
    }
}