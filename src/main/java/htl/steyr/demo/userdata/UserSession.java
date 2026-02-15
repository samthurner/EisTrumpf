package htl.steyr.demo.userdata;

import java.net.ServerSocket;
import java.net.Socket;

public class UserSession {

    private static UserData userData;

    private static boolean isHost;
    private static ServerSocket serverSocket;
    private static Socket socket;

    public static void setUserData(UserData data) {
        userData = data;
    }

    public static UserData getUserData() {
        return userData;
    }

    public static void setIsHost(boolean isHost) {
        UserSession.isHost = isHost;
    }

    public static boolean isHost() {
        return isHost;
    }

    public static void setServerSocket(ServerSocket serverSocket) {
        UserSession.serverSocket = serverSocket;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }
    public static void setSocket(Socket socket) {
        UserSession.socket = socket;
    }
}
