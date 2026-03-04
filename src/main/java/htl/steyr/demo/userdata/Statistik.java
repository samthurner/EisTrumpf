package htl.steyr.demo.userdata;


import htl.steyr.demo.gameTimer.GameTimer;

public class Statistik {

    private int games_won;
    private int games_lost;
    private double winRate;
    private int gameTimeGame;
    private UserData userData;
    private GameTimer gameTimer;


    public Statistik(UserData userData, GameTimer gameTimer) {
        this.userData = userData;
        this.gameTimer = gameTimer;
    }


    public void gameWonStat(){

    }

    public void gamePlayedStat(){

    }

    public void winRateStat(){

    }

    public int gameTimeStat(){
        gameTimeGame = gameTimer.getElapsedSeconds();
        int gameTimeJson =  userData.getPlaytime();

        int gameTime = gameTimeJson + gameTimeGame;
        userData.setPlaytime(gameTime);

        userData.writeToJson();

        return gameTime;
    }
}
