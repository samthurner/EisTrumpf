package htl.steyr.demo.music;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {

    private MediaPlayer backgroundPlayer;

    public void playSong(){

        Media media = new Media(getClass().getResource("/htl/steyr/demo/music/mozart.mp3").toExternalForm());

        backgroundPlayer = new MediaPlayer(media);
        backgroundPlayer.setAutoPlay(true);
        backgroundPlayer.play();
    }



}
