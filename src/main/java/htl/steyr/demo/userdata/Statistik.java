package htl.steyr.demo.userdata;


import htl.steyr.demo.gameTimer.GameTimer;

public class Statistik {

    private UserData userData;
    private GameTimer gameTimer;


    public Statistik(UserData userData, GameTimer gameTimer) {
        this.userData = userData;
        this.gameTimer = gameTimer;
    }


    public void gameWonStat() {
        userData.setGames_won(userData.getGames_won() + 1);

        int streak = userData.getWinstreak() + 1;
        userData.setWinstreak(streak);

        if (streak > userData.getHighest_winstreak()) {
            userData.setHighest_winstreak(streak);
        }

        userData.writeToJson();
    }

    public void gameLostStat() {
        userData.setGames_lost(userData.getGames_lost() + 1);

        userData.setWinstreak(0);

        userData.writeToJson();
    }

    public double winRateStat() {

        int wins = userData.getGames_won();
        int losses = userData.getGames_lost();

        int total = wins + losses;

        if (total == 0) {
            return 0;
        }

        return (wins * 100.0) / total;

    }

    public int gamePlayedStat() {
        int wins = userData.getGames_won();
        int losses = userData.getGames_lost();

        return wins + losses;
    }

    public int gameTimeStat() {
        if (gameTimer == null) {
            return userData.getPlaytime();
        }

        int gameTimeGame = gameTimer.getElapsedSeconds();
        int totalGametime = userData.getPlaytime() + gameTimeGame;
        userData.setPlaytime(totalGametime);
        userData.writeToJson();
        return totalGametime;
    }
}
