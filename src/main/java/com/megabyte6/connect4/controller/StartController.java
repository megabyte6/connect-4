package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.model.Player;
import com.megabyte6.connect4.util.SceneManager;
import com.megabyte6.connect4.util.tuple.Pair;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
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
                    App.setPlayer1(new Player(name));
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
                    App.setPlayer2(new Player(name));
                    SceneManager.switchScenes("Game", Duration.millis(400));
                },
                () -> setDisable(false)
        );
    }

    private void playerNameInputPopup(String promptText, String defaultText, Consumer<String> onOk, Runnable onCancel) {
        Pair<Node, Controller> loadedData = SceneManager.loadFXMLAndController("TextInput");
        Node root = loadedData.a();
        TextInputController controller = (TextInputController) loadedData.b();

        controller.setPromptText(promptText);
        controller.setDefaultText(defaultText);
        controller.setOnOK(onOk);
        controller.setOnCancel(onCancel);

        SceneManager.addScene(root);
    }

    @Override
    public void setDisable(boolean disabled) {
        root.setDisable(disabled);
        root.setOpacity(disabled ? App.DISABLED_OPACITY : 1);
    }
}
