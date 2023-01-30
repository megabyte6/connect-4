package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.util.SceneManager;
import javafx.fxml.FXML;
import javafx.util.Duration;

public class StartController {

    @FXML
    private void startButtonPressed() {
        SceneManager.switchScenes("Game", Duration.millis(400));
    }

}
