module com.megabyte6.connect4 {
    requires javafx.fxml;
    requires com.electronwill.nightconfig.core;
    requires transitive javafx.controls;

    opens com.megabyte6.connect4.controller to javafx.fxml;
    opens com.megabyte6.connect4.controller.dialog to javafx.fxml;

    exports com.megabyte6.connect4;
    exports com.megabyte6.connect4.controller;
    exports com.megabyte6.connect4.controller.dialog;
    exports com.megabyte6.connect4.model;
    exports com.megabyte6.connect4.util;
    exports com.megabyte6.connect4.util.tuple;
}