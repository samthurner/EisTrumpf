package htl.steyr.demo.controller;

import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.userdata.UserData;
import htl.steyr.demo.userdata.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

public class SettingsController {

    @FXML
    private ToggleButton changeTheme;

    public void Initialize() {
        boolean isDark = UserSession.getInstance().isDarkMode();
        changeTheme.setSelected(isDark);
    }
    @FXML
    public void onThemechange(ActionEvent actionEvent) {
        boolean dark = changeTheme.isSelected();

        UserSession.getInstance().setDarkMode(dark);

        var scene = changeTheme.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            if (dark){
                scene.getStylesheets().add(getClass().getResource("/stylesheets/darkmode.css").toExternalForm());
            }
        scene.getStylesheets().clear();
        if (dark) {
            scene.getStylesheets().add(getClass().getResource("/stylesheets/darkmode.css").toExternalForm());
        } else{
            scene.getStylesheets().add(getClass().getResource("/stylesheets/whitemode.css").toExternalForm());

        }
    }

    @FXML
    public void onChangeNameClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("name-alert");
    }

    @FXML
    public void onResetStatsClicked(ActionEvent actionEvent) {
        UserData user = UserSession.getInstance().getUserData();
        user.resetStats();
        user.writeToJson();
        changeTheme.setSelected(user.isDarkmode());
        System.out.println("Stats wurden zurückgesetzt.");
    }

    @FXML
    public void onBackClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("menu");
    }
}
