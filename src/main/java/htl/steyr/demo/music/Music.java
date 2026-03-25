package htl.steyr.demo.music;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Music {

    private static MediaPlayer backgroundPlayer;
    private static boolean isStarted = false;

    private static List<String> songs = List.of(
            "/htl/steyr/demo/music/track1.mp3",
            "/htl/steyr/demo/music/track2.mp3",
            "/htl/steyr/demo/music/track3.mp3",
            "/htl/steyr/demo/music/track4.mp3",
            "/htl/steyr/demo/music/track5.mp3",
            "/htl/steyr/demo/music/track6.mp3"
    );

    private static int currentIndex = 0;
    private static Stack<Integer> history = new Stack<>();
    private static Random random = new Random();

    private static boolean controlsVisible = false;

    public static void setControlsVisible(boolean visible) {
        controlsVisible = visible;
    }

    public static boolean isControlsVisible() {
        return controlsVisible;
    }
    public static void startMusic() {
        isStarted = true;
        if (backgroundPlayer == null) {
            playSong(0);
        }
    }
    public static void playSong(int index){
        if (!isStarted) {
            return;
        }

        if (backgroundPlayer != null){
            backgroundPlayer.stop();
        }

        currentIndex = index;

        Media media = new Media(
          Music.class.getResource(songs.get(currentIndex)).toExternalForm()
        );

        backgroundPlayer = new MediaPlayer(media);
        backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundPlayer.setVolume(getVolume());
        backgroundPlayer.play();
    }

    public static void playNextRandom(){
        if (songs.size() <= 1){
            return;
        }
        history.push(currentIndex);

        int next;
        do {
            next = random.nextInt(songs.size());
        } while (next == currentIndex);

        playSong(next);

    }

    public static void playPrevious() {
        if (!history.isEmpty()) {
            int prev = history.pop();
            playSong(prev);
        }
    }

    public static void setVolume(double volume){
        if(backgroundPlayer != null){
            backgroundPlayer.setVolume(volume);
        }
    }

    public static double getVolume(){
        if(backgroundPlayer != null){
            return backgroundPlayer.getVolume();
        }
        return 0.5;
    }
}
