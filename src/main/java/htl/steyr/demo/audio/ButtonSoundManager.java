package htl.steyr.demo.audio;

import javafx.scene.media.AudioClip;

public class ButtonSoundManager {
    // Singleton-Instanz, damit nur ein SoundManager existiert
    private static ButtonSoundManager instance;

    // Speichert den Klick-Sound, der abgespielt wird
    private AudioClip clickSound;

    // Privater Konstruktor (Singleton), lädt die Sound-Datei aus den Ressourcen
    private ButtonSoundManager(){
        clickSound = new AudioClip(
                getClass().getResource("/htl/steyr/demo/sounds/button_click.mp3").toExternalForm()
        );
    }

    // Gibt die eine Instanz zurück (erstellt sie falls nötig)
    public static ButtonSoundManager getInstance() {
        if (instance == null) {
            instance = new ButtonSoundManager();
        }
        return instance;
    }

    // Spielt den gespeicherten Klick-Sound ab
    public void playClick(){
        clickSound.play();
    }
}