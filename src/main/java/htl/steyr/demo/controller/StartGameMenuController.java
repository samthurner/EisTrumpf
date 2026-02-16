package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class StartGameMenuController {

    public TextField hostPortField;
    public Button hostGoButton;
    public TextField joinIpField;
    public TextField joinPortField;
    public Button joinButton;
    public Button exitButton;

    public void onJoinButtonClicked(ActionEvent actionEvent) {

    }

    public void onHostButtonClicked(ActionEvent actionEvent) {
        if (!hostPortField.getText().isEmpty()) {

            /**
             * @Todo: Einen weg finden, die Numberformatexception zu checken und dem nutzer mitzuteilen
              */
            int port = Integer.parseInt(hostPortField.getText());
            boolean isCorrectPort = true;

            if (port < 1023 || port > 65534) {

                String text = "";
                isCorrectPort = false;

                if (port < 1023) {
                    text = "Der eingegebene Port muss über 1023 sein.";

                } else {
                    text = "Der eingegebene Port muss unter 65535 sein.";

                }
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Fehler");
                alert.setHeaderText(text);
                alert.showAndWait();
            }
            if(isCorrectPort) {
                ViewSwitcher.switchTo("loading-screen");
            }

        }

    }

    public void onExitButtonClicked(ActionEvent actionEvent) {

    }
}