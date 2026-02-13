package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.userdata.UserData;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.sql.SQLOutput;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;
    public UserData userData;

    @FXML
    private void onLoginClicked() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fehler");
            alert.setHeaderText("Bitte geben Sie einen Benutzernamen ein.");
            alert.showAndWait();
        }else{
            ViewSwitcher.switchTo("menu");
            userData = new UserData(username);
            userData.writeToJson();
            System.out.println("Spielername: " + username);
            System.out.println(userData.getUsername());
            System.out.println(userData.getWinstreak());
            System.out.println(userData.getHighest_winstreak());
            System.out.println(userData.getGames_won());
            System.out.println(userData.getGames_lost());
            System.out.println(userData.getPlaytime());
        }

    }
}
