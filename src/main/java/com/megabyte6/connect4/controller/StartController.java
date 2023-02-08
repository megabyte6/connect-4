package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.controller.dialog.TextPromptController;
import com.megabyte6.connect4.model.Player;
import com.megabyte6.connect4.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.function.Consumer;

public class StartController implements Controller {

    @FXML
    private AnchorPane root;

    @FXML
    private void initialize() {
        root.setOnKeyPressed(event -> {
            if (event.isShortcutDown()) {
                if (event.getCode() == KeyCode.S)
                    handleSettingsButton();
            }
        });

        root.requestFocus();
    }

    @FXML
    private void startButtonPressed() {
        setDisable(true);
        getPlayer1Name();
    }

    private void getPlayer1Name() {
        playerNameInputPopup(
                "What's Player 1's name?",
                App.getPlayer1().getName(),
                (text) -> {
                    String name = text.isEmpty() || text.isBlank() ? "Player 1" : text;
                    if (App.getPlayer1().equals(Player.NONE)) {
                        App.setPlayer1(new Player(name, Color.YELLOW));
                    } else {
                        App.getPlayer1().setName(name);
                    }

                    getPlayer2Name();
                },
                () -> setDisable(false)
        );
    }

    private void getPlayer2Name() {
        playerNameInputPopup(
                "What's Player 2's name",
                App.getPlayer2().getName(),
                (text) -> {
                    String name = text.isEmpty() || text.isBlank() ? "Player 2" : text;
                    if (App.getPlayer2().equals(Player.NONE)) {
                        App.setPlayer2(new Player(name, Color.RED));
                    } else {
                        App.getPlayer2().setName(name);
                    }

                    SceneManager.switchScenes("Game", Duration.millis(400));
                },
                () -> setDisable(false)
        );
    }

    private void playerNameInputPopup(String promptText, String defaultText, Consumer<String> onOk, Runnable onCancel) {
        final var loadedData = SceneManager.loadFXMLAndController("dialog/TextPrompt");
        final Node root = loadedData.a();
        final TextPromptController controller = (TextPromptController) loadedData.b();

        controller.setPromptText(promptText);
        controller.setDefaultText(defaultText);
        controller.selectTextByDefault(true);
        controller.setOnOk(onOk);
        controller.setOnCancel(onCancel);

        SceneManager.addScene(root);
    }

    @FXML
    private void handleSettingsButton() {
        setDisable(true);

        final var loadedData = SceneManager.loadFXMLAndController("Settings");
        final Node root = loadedData.a();
        final SettingsController controller = (SettingsController) loadedData.b();
        controller.setOnClosed(() -> setDisable(false));

        SceneManager.addScene(root);
    }

    @Override
    public void setDisable(boolean disabled) {
        root.setDisable(disabled);
        root.setOpacity(disabled ? App.DISABLED_OPACITY : 1);
    }
}
