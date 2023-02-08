package com.megabyte6.connect4.controller.dialog;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.controller.Controller;
import com.megabyte6.connect4.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.function.Consumer;

public class TextPromptController implements Controller {

    private Consumer<String> onOk = (text) -> {
    };
    private Runnable onCancel = () -> {
    };

    private boolean selectText = false;

    @FXML
    private AnchorPane root;

    @FXML
    private Label promptLabel;

    @FXML
    private TextField textField;

    @FXML
    public void initialize() {
        textField.setOnAction((event) -> {
            SceneManager.removeTopScene();
            onOk.accept(textField.getText());
        });

        textField.requestFocus();
    }

    public void setPromptText(String promptText) {
        promptLabel.setText(promptText);
    }

    public void setDefaultText(String text) {
        textField.setText(text);

        if (selectText)
            textField.selectAll();
    }

    public void selectTextByDefault(boolean selectText) {
        this.selectText = selectText;

        textField.selectAll();
    }

    public void setOnOk(Consumer<String> run) {
        onOk = run;
    }

    public void setOnCancel(Runnable run) {
        onCancel = run;
    }

    @FXML
    private void handleOkButton() {
        SceneManager.removeTopScene();
        onOk.accept(textField.getText());
    }

    @FXML
    private void handleCancelButton() {
        SceneManager.removeTopScene();
        onCancel.run();
    }

    @Override
    public void setDisable(boolean disabled) {
        root.setDisable(disabled);
        root.setOpacity(disabled ? App.DISABLED_OPACITY : 1);
    }

}
