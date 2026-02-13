package htl.steyr.demo.userdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserData {
    private String username;
    private int winstreak;
    private int highest_winstreak;
    private int games_won;
    private int games_lost;
    private int playtime;

    public UserData(String username) {
        setUsername(username);
        loadFromJson(username);
    }

    public void loadFromJson(String username) {
        try {
            File file = new File(username + ".json");

            if (!file.exists()) {

                setWinstreak(0);
                setHighest_winstreak(0);
                setGames_won(0);
                setGames_lost(0);
                setPlaytime(0);
            } else {
                Gson g = new Gson();
                FileReader reader = new FileReader(file);

                UserData loaded = g.fromJson(reader, UserData.class);
                reader.close();

                setWinstreak(loaded.getWinstreak());
                setHighest_winstreak(loaded.getHighest_winstreak());
                setGames_won(loaded.getGames_won());
                setGames_lost(loaded.getGames_lost());
                setPlaytime(loaded.getPlaytime());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToJson() {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(this);
        try {
            File file = new File(getUsername() + ".json");

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
