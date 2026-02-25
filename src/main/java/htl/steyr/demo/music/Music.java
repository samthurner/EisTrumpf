package htl.steyr.demo.music;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {

    private static MediaPlayer backgroundPlayer;

    public static void playSong(){

        Media media = new Media(Music.class.getResource("/htl/steyr/demo/music/mozart.mp3").toExternalForm());

        backgroundPlayer = new MediaPlayer(media);
        backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundPlayer.setVolume(0.5);
        backgroundPlayer.play();
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
