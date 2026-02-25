module htl.steyr.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;

    opens htl.steyr.demo to javafx.fxml;
    opens htl.steyr.demo.fxml to javafx.fxml;
    opens htl.steyr.demo.controller to javafx.fxml;

    opens htl.steyr.demo.userdata to com.google.gson;

    opens image;

    exports htl.steyr.demo;
    exports htl.steyr.demo.controller;
}
