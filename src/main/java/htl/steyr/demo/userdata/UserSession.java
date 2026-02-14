package htl.steyr.demo.userdata;

public class UserSession {

    private static UserData userData;

    public static void setUserData(UserData data) {
        userData = data;
    }

    public static UserData getUserData() {
        return userData;
    }
}
