module htl.steyr.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens htl.steyr.demo to javafx.fxml;
    exports htl.steyr.demo;
}