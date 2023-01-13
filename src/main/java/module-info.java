module connect_four {
    requires javafx.fxml;

    requires transitive javafx.controls;

    opens com.megabyte6.connect4.controller to javafx.fxml;

    exports com.megabyte6.connect4;
    exports com.megabyte6.connect4.controller;
    exports com.megabyte6.connect4.model;
    exports com.megabyte6.connect4.util;
}