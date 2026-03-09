package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.userdata.UserData;
import htl.steyr.demo.userdata.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.File;

public class NameAlertController {
    @FXML
    private TextField nameField;

    @FXML
    public void onOkayClicked() {
        String newName = nameField.getText();

        if (newName != null && !newName.isBlank()) {
            UserData user = UserSession.getInstance().getUserData();
            String oldName = user.getUsername();
            user.setUsername(newName);
            user.writeToJson();
            File directory = new File("Json");
            for(File file : directory.listFiles()){
                if(file.getName().equals(oldName + ".json")){
                    file.delete();
                }
            }

        }

        ViewSwitcher.switchTo("settings-pane");
    }

    @FXML
    public void onCancelClicked() {
        ViewSwitcher.switchTo("settings-pane");
    }
}
