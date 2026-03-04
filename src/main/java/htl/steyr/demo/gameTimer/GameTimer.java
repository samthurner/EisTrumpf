package htl.steyr.demo.gameTimer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class GameTimer {

    public Label timeLabel;
    Timeline timeline;
    private int elapsedSeconds = 0;

    public GameTimer(Label timeLabel) {
        this.timeLabel = timeLabel;
    }


    //Methode um den Timer zu starten
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

    public void stop() {
        if  (timeline != null) {
            timeline.stop();
        }
    }

    public static String formatToHHMMSS(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }
}
