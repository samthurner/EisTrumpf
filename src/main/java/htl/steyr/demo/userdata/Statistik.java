package htl.steyr.demo.userdata;


import htl.steyr.demo.gameTimer.GameTimer;


/**
 * Berechnet und verwaltet Statistiken des Users.
 * Schreibt Änderungen in UserData und unterstützt Gewinn-/Verluststatistiken.
 */
public class Statistik {
    // Referenz auf User-Daten
    private UserData userData;

    // Referenz auf GameTimer für aktuelle Spielzeit
    private GameTimer gameTimer;


    public Statistik(UserData userData, GameTimer gameTimer) {
        this.userData = userData;
        this.gameTimer = gameTimer;
    }

    /**
     * Aktualisiert Statistik nach gewonnenem Spiel:
     * - erhöht Spiele gewonnen
     * - erhöht aktuelle Gewinnserie, wenn nötig
     * - prüft und speichert höchste Gewinnserie
     * - speichert in JSON
     */
    public void gameWonStat() {
        userData.setGames_won(userData.getGames_won() + 1);

        int streak = userData.getWinstreak() + 1;
        userData.setWinstreak(streak);

        if (streak > userData.getHighest_winstreak()) {
            userData.setHighest_winstreak(streak);
        }

        userData.writeToJson();
    }

    /**
     * Aktualisiert Statistik nach verlorenem Spiel:
     * - erhöht Spiele verloren
     * - setzt Gewinnserie zurück
     * - speichert in JSON
     */
    public void gameLostStat() {
        userData.setGames_lost(userData.getGames_lost() + 1);

        userData.setWinstreak(0);

        userData.writeToJson();
    }

    /**
     * Berechnet Gewinnrate in Prozent (0-100).
     */
    public double winRateStat() {

        int wins = userData.getGames_won();
        int losses = userData.getGames_lost();

        int total = wins + losses;

        if (total == 0) {
            return 0;
        }

        return (wins * 100.0) / total;

    }

    /**
     * Gibt die Gesamtanzahl gespielter Spiele zurück.
     */
    public int gamePlayedStat() {
        int wins = userData.getGames_won();
        int losses = userData.getGames_lost();

        return wins + losses;
    }

    /**
     * Berechnet und aktualisiert die gesamte Spielzeit:
     * - addiert aktuelle Spielzeit vom Timer zu gespeicherter Zeit
     * - speichert in JSON
     */
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
