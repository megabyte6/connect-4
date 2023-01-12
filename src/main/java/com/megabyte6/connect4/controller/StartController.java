package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.util.SceneManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Duration;

public class StartController implements Controller {

    private StartController() {
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void startButtonPressed(ActionEvent event) {
        SceneManager.switchScenes("Game", Duration.millis(400));
    }

}
