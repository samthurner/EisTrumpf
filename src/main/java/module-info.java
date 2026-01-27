module htl.steyr.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens htl.steyr.demo to javafx.fxml;
    exports htl.steyr.demo;
}