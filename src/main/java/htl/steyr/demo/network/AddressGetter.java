package htl.steyr.demo.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class AddressGetter {

    public static String getIPAddress() {
        String ipAddress = "";
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            ipAddress = socket.getLocalAddress().getHostAddress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ipAddress;
    }
}
