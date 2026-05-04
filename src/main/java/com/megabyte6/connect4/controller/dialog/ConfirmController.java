package com.megabyte6.connect4.controller.dialog;

import com.megabyte6.connect4.controller.Controller;
import com.megabyte6.connect4.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

public class ConfirmController implements Controller {

    @FXML
    private Label textLabel;

    @Setter
    private Runnable onOk = () -> {
    };
    @Setter
    private Runnable onCancel = () -> {
    };

    @FXML
    private void initialize() {
        textLabel.setText("The developer forgot to set this text :)");
    }

    public void setText(String text) {
        textLabel.setText(text);
    }

    @FXML
    private void handleOkButton() {
        SceneManager.removeTopScene();
        onOk.run();
    }

    @FXML
    private void handleCancelButton() {
        SceneManager.removeTopScene();
        onCancel.run();
    }

    @Override
    public void setDisable(boolean disabled) {
    }

}
