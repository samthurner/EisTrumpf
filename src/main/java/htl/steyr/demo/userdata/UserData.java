package htl.steyr.demo.userdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UserData {
    private String username;
    private int winstreak;
    private int highest_winstreak;
    private int games_won;
    private int games_lost;
    private int playtime;
    private boolean darkmode;


    public UserData(String username) {
        setUsername(username);
        loadFromJson(username);
    }

    public void loadFromJson(String username) {
        try {
            File directory = new File("Json");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, username + ".json");

            if (!file.exists()) {
                resetStats();
                writeToJson();
                return;
            }

            Gson gson = new Gson();

            try (FileReader reader = new FileReader(file)) {
                UserData loaded = gson.fromJson(reader, UserData.class);

                this.winstreak = loaded.winstreak;
                this.highest_winstreak = loaded.highest_winstreak;
                this.games_won = loaded.games_won;
                this.games_lost = loaded.games_lost;
                this.playtime = loaded.playtime;
                this.darkmode = loaded.darkmode;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToJson() {
        try {
            File directory = new File("Json");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, username + ".json");

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(this, writer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getPlaytime() {
        return playtime;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public int getGames_lost() {
        return games_lost;
    }

    public void setGames_lost(int games_lost) {
        this.games_lost = games_lost;
    }

    public int getGames_won() {
        return games_won;
    }

    public void setGames_won(int games_won) {
        this.games_won = games_won;
    }

    public int getHighest_winstreak() {
        return highest_winstreak;
    }

    public void setHighest_winstreak(int highest_winstreak) {
        this.highest_winstreak = highest_winstreak;
    }

    public int getWinstreak() {
        return winstreak;
    }

    public void setWinstreak(int winstreak) {
        this.winstreak = winstreak;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isDarkmode() { return darkmode; }

    public void setDarkmode(boolean darkmode) { this.darkmode = darkmode; }


    public void resetStats() {
        setWinstreak(0);
        setHighest_winstreak(0);
        setGames_won(0);
        setGames_lost(0);
        setPlaytime(0);
        this.setDarkmode(false);
    }

}
