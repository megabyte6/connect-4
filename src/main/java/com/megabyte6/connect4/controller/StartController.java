package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.controller.dialog.TextPromptController;
import com.megabyte6.connect4.model.Player;
import com.megabyte6.connect4.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.function.Consumer;

public class StartController implements Controller {

    @FXML
    private AnchorPane root;

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
                    App.setPlayer1(new Player(name, Color.YELLOW));
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
                    App.setPlayer2(new Player(name, Color.RED));
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
        controller.setOnOk(onOk);
        controller.setOnCancel(onCancel);

        SceneManager.addScene(root);
    }

    @Override
    public void setDisable(boolean disabled) {
        root.setDisable(disabled);
        root.setOpacity(disabled ? App.DISABLED_OPACITY : 1);
    }
}
