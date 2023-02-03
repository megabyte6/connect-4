package com.megabyte6.connect4.controller;

import javafx.fxml.FXML;

public class SettingsController implements Controller {

    private Runnable runAfter = () -> {
    };

    public void setOnClosed(Runnable run) {
        runAfter = run;
    }

    @FXML
    private void handleCloseButton() {
        runAfter.run();
    }

    @Override
    public void setDisable(boolean disabled) {
    }
}
