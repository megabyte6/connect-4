package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.model.Player;
import com.megabyte6.connect4.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class GameFinishedController implements Controller {

    private Runnable runOnClose = () -> {
    };

    @FXML
    private AnchorPane root;

    @FXML
    private Label label;

    @FXML
    private void initialize() {
        if (App.getWinner().equals(Player.NONE.get())) {
            label.setText("It's a tie!");
        } else {
            label.setText(App.getWinner().getName() + " won!");
        }
    }

    public void setOnClose(Runnable run) {
        runOnClose = run;
    }

    @FXML
    private void handleBackToStartScreen() {
        SceneManager.switchScenes("Start", Duration.millis(400));
    }

    @FXML
    private void handleClose() {
        SceneManager.removeTopScene();
        runOnClose.run();
    }

    @Override
    public void setDisable(boolean disabled) {
        root.setDisable(disabled);
        root.setOpacity(disabled ? App.DISABLED_OPACITY : 1);
    }

}
