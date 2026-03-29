package htl.steyr.demo.music;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Verwaltet die Hintergrundmusik der Anwendung.
 * Bietet Funktionen zum Starten, Wechseln (zufällig) und Lautstärke steuern.
 */
public class Music {
    // Aktueller MediaPlayer für die Hintergrundmusik
    private static MediaPlayer backgroundPlayer;

    // Gibt an, ob die Musik bereits gestartet wurde
    private static boolean isStarted = false;

    // Liste der verfügbaren Songs
    private static List<String> songs = List.of(
            "/htl/steyr/demo/music/track1.mp3",
            "/htl/steyr/demo/music/track2.mp3",
            "/htl/steyr/demo/music/track3.mp3",
            "/htl/steyr/demo/music/track4.mp3",
            "/htl/steyr/demo/music/track5.mp3",
            "/htl/steyr/demo/music/track6.mp3"
    );

    private static int currentIndex = 0;

    // Stack speichert vorherige Songs für "Zurück"-Funktion
    private static Stack<Integer> history = new Stack<>();

    // Zufallszahlengenerator für zufällige Songauswahl
    private static Random random = new Random();

    // Steuert, ob Musiksteuerung im UI sichtbar ist
    private static boolean controlsVisible = false;

    // Gibt zurück, ob die Steuerung sichtbar ist
    public static boolean isControlsVisible() {
        return controlsVisible;
    }

    //startet die Musik
    public static void startMusic() {
        isStarted = true;
        if (backgroundPlayer == null) {
            playSong(0);
        }
    }

    /**
     * Spielt einen bestimmten Song anhand seines Index ab.
     * Stoppt vorher ggf. laufende Musik.
     */
    public static void playSong(int index){
        if (!isStarted) {
            return;
        }

        if (backgroundPlayer != null){
            backgroundPlayer.stop();
        }

        currentIndex = index;

        // Lädt die Mediendatei aus den Ressourcen
        Media media = new Media(
          Music.class.getResource(songs.get(currentIndex)).toExternalForm()
        );

        // Erstellt neuen Player und startet Wiedergabe in Dauerschleife
        backgroundPlayer = new MediaPlayer(media);
        backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundPlayer.setVolume(getVolume());
        backgroundPlayer.play();
    }

    /**
     * Spielt einen zufälligen nächsten Song.
     * Speichert aktuellen Song im Verlauf.
     */
    public static void playNextRandom(){
        if (songs.size() <= 1){
            return;
        }
        history.push(currentIndex);

        int next;
        do {
            next = random.nextInt(songs.size());
        } while (next == currentIndex); // verhindert Wiederholung

        playSong(next);

    }

    /**
     * Spielt den vorherigen Song aus dem Verlauf.
     */
    public static void playPrevious() {
        if (!history.isEmpty()) {
            int prev = history.pop();
            playSong(prev);
        }
    }

    /**
     * Setzt die Lautstärke (0.0 - 1.0).
     */
    public static void setVolume(double volume){
        if(backgroundPlayer != null){
            backgroundPlayer.setVolume(volume);
        }
    }

    /**
     * Gibt die aktuelle Lautstärke zurück.
     * Standardwert: 0.5
     */
    public static double getVolume(){
        if(backgroundPlayer != null){
            return backgroundPlayer.getVolume();
        }
        return 0.5;
    }
}
