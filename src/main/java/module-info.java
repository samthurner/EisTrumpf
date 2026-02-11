module htl.steyr.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens htl.steyr.demo to javafx.fxml;
    opens htl.steyr.demo.fxml to javafx.fxml;
    opens image;

    exports htl.steyr.demo;
    exports htl.steyr.demo.controller;
    opens htl.steyr.demo.controller to javafx.fxml;
}
