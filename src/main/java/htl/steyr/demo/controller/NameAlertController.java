package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.userdata.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NameAlertController {

    @FXML
    private TextField nameField;

    @FXML
    public void onOkayClicked() {
        String newName = nameField.getText();

        if (newName != null && !newName.isBlank()) {
            UserSession.getInstance().getUserData().setName(newName);
            System.out.println("Neuer Name: " + newName);
        }

        ViewSwitcher.switchTo("settings-pane");
    }

    @FXML
    public void onCancelClicked() {
        ViewSwitcher.switchTo("settings-pane");
    }
}
