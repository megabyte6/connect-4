module com.megabyte6.connect4 {
    requires static lombok;

    requires javafx.fxml;
    requires transitive javafx.controls;

    requires com.fasterxml.jackson.databind;

    opens com.megabyte6.connect4.controller to javafx.fxml;
    opens com.megabyte6.connect4.controller.dialog to javafx.fxml;

    exports com.megabyte6.connect4;
    exports com.megabyte6.connect4.controller;
    exports com.megabyte6.connect4.controller.dialog;
    exports com.megabyte6.connect4.model;
    exports com.megabyte6.connect4.util;
    exports com.megabyte6.connect4.util.tuple;
}
