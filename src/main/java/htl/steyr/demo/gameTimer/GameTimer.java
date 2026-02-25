package htl.steyr.demo.gameTimer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class GameTimer {

    public Label timeLabel;
    Timeline timeline;
    public int seconds;
    public int minutes;

    public GameTimer(Label timeLabel) {
        this.timeLabel = timeLabel;
    }


    //Methode um den Timer zu starten
    public void start() {

        seconds = 0;
        minutes = 0;

        //nach jeder Sekunde wird die Sekunde hochgezählt
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    seconds++;

                    //wenn es 60 Sekunden sind, dann wird Sekunde wieder auf 0 gesetzt und Minute um 1 erhöht
                    if (seconds == 60) {
                        seconds = 0;
                        minutes++;
                    }
                })
        );

        //Timer zählt unendlich hoch
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

}
