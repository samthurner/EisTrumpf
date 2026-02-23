package htl.steyr.demo.userdata;

import htl.steyr.demo.network.GameClient;
import htl.steyr.demo.network.GameServer;

public class UserSession {

    private static UserData userData;

    private static boolean isHost;
    private static GameServer gameServer;
    private static GameClient gameClient;

    public static void setUserData(UserData data) {
        userData = data;
    }

    public static UserData getUserData() {
        return userData;
    }

    public static void setHost(GameServer server) {
        isHost = true;
        gameServer = server;
        gameClient = null;
    }

    public static void setClient(GameClient client) {
        isHost = false;
        gameClient = client;
        gameServer = null;
    }

    public static boolean isHost() {
        return isHost;
    }

    public static GameServer getGameServer() {
        return gameServer;
    }

    public static GameClient getGameClient() {
        return gameClient;
    }

    public static void clear() {
        userData = null;
        gameServer = null;
        gameClient = null;
        isHost = false;
    }
}