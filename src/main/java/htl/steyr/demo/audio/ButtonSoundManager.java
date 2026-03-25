package htl.steyr.demo.audio;

import javafx.scene.media.AudioClip;

public class ButtonSoundManager {
    private static ButtonSoundManager instance;

    private AudioClip clickSound;

    private ButtonSoundManager(){
        clickSound = new AudioClip(
          getClass().getResource("/htl/steyr/demo/sounds/button_click.mp3").toExternalForm()
        );
    }
    public static ButtonSoundManager getInstance() {
        if (instance == null) {
            instance = new ButtonSoundManager();
        }
        return instance;
    }

    public  void playClick(){
        clickSound.play();
    }
}
