package htl.steyr.demo.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;

public class LoadingScreenController {

    public Label loadingText;
    public Separator redLine;
    public ProgressIndicator loadingCircle;
    public ImageView logoImage;
    public Button exitButton;

    private final String[] texts = {
    };

    public void initialize() {
    }

    private String randomText() {
        return "";
    }

    public void onExit() {
    }
}
