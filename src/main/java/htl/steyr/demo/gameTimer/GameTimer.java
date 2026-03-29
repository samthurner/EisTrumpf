package htl.steyr.demo.gameTimer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Timer für das Spiel, der die Zeit in Sekunden zählt
 * und als HH:MM:SS in einem Label anzeigt.
 */
public class GameTimer {

    // Label im UI zur Anzeige der Zeit
    public Label timeLabel;

    // Timeline für wiederholtes Hochzählen jeder Sekunde
    Timeline timeline;

    // Gesamtzeit in Sekunden seit Timerstart
    private int elapsedSeconds = 0;


    // Label für Anzeige an den Konstruktor übergeben
    public GameTimer(Label timeLabel) {
        this.timeLabel = timeLabel;
    }

    /**
     * //Methode um den Timer zu starten:
     * - erhöht jede Sekunde elapsedSeconds
     * - aktualisiert das Label
     * - läuft unendlich weiter
     */
    public void start() {
        //nach jeder Sekunde wird die Sekunde hochgezählt
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    elapsedSeconds++;
                    timeLabel.setText(formatToHHMMSS(elapsedSeconds));
                })
        );

        //Timer zählt unendlich hoch
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Stoppt den Timer, falls er läuft
     */
    public void stop() {
        if  (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Formatiert die Zeit von Sekunden in HH:MM:SS
     */
    public static String formatToHHMMSS(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Gibt die verstrichene Zeit in Sekunden zurück
     */
    public int getElapsedSeconds() {
        return elapsedSeconds;
    }
}
