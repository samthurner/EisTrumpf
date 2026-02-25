package htl.steyr.demo.userdata;

public class UserSession {

    private static UserData userData;

    public static void setUserData(UserData data) {
        userData = data;
    }

    public static UserData getUserData() {
        return userData;
    }

    private boolean darkMode;

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    private static UserSession instance;


    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUsername(String newName) {
        userData.setUsername(newName);
        userData.writeToJson();
    }
}
