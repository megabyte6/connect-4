package com.megabyte6.connect4.util;

import static javafx.util.Duration.millis;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneManager {

    private static final String RESOURCE_PATH = "/com/megabyte6/connect4/view/";

    private static Stage stage;
    private static Scene scene;
    private static StackPane sceneStack;
    private static Color backgroundColor;

    private SceneManager() {
    }

    public static void init(Stage stage, String fxmlName) {
        init(stage, fxmlName, Color.WHITE);
    }

    public static void init(Stage stage, String fxmlName, Color backgroundColor) {
        SceneManager.stage = stage;

        SceneManager.sceneStack = new StackPane();
        addScene(loadFXML(fxmlName));
        SceneManager.scene = new Scene(SceneManager.sceneStack);

        SceneManager.backgroundColor = backgroundColor;

        SceneManager.stage.setScene(SceneManager.getScene());
    }

    private static Node loadFXML(String fxmlName) {
        if (fxmlName == null)
            return null;

        final String path = RESOURCE_PATH + fxmlName + ".fxml";
        final Node root;

        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource(path));
            root = fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("ERROR: Cannot read file '" + path + "'");
            e.printStackTrace();
            return null;
        }

        return root;
    }

    public static void switchScenes(String fxmlName, Duration totalTransitionDuration) {
        // Get old scene.
        final Scene oldScene = SceneManager.scene;
        oldScene.setFill(SceneManager.backgroundColor);
        final StackPane oldSceneStack = SceneManager.sceneStack;

        // Load new scene.
        final Node fxmlData = loadFXML(fxmlName);
        if (fxmlData == null)
            return;
        final StackPane newSceneStack = new StackPane();
        newSceneStack.getChildren().add(fxmlData);
        newSceneStack.setOpacity(0);

        final Scene newScene = new Scene(newSceneStack);
        newScene.setFill(SceneManager.backgroundColor);

        // Set up fade transitions.
        final FadeTransition oldSceneFadeOut = new FadeTransition(
                totalTransitionDuration.divide(2), oldSceneStack);
        oldSceneFadeOut.setFromValue(1);
        oldSceneFadeOut.setToValue(0);

        final FadeTransition newSceneFadeIn = new FadeTransition(
                totalTransitionDuration.divide(2), newSceneStack);
        newSceneFadeIn.setFromValue(0);
        newSceneFadeIn.setToValue(1);

        // Play transitions.
        oldSceneFadeOut.setOnFinished(event -> {
            // Swap scenes between transitions.
            SceneManager.scene = newScene;
            SceneManager.sceneStack = newSceneStack;
            SceneManager.stage.setScene(SceneManager.scene);
            // Start the new scene's fade-in animation.
            newSceneFadeIn.play();
        });
        oldSceneFadeOut.play();
    }

    public static void popup(String text) {
        popup(text, millis(250), millis(1000));
    }

    public static void popup(String text, Duration transitionDuration, Duration pauseDuration) {
        final Label popup = new Label(text);
        popup.setTextFill(Color.BLACK);
        popup.setFont(new Font("System", 18));
        popup.setPadding(new Insets(5, 10, 5, 10));
        popup.setBackground(new Background(new BackgroundFill(
                Color.WHITE,
                new CornerRadii(25),
                null)));

        popup(popup, transitionDuration, pauseDuration);
    }

    public static void popup(Label popup, Duration transitionDuration, Duration pauseDuration) {
        final FadeTransition fadeIn = new FadeTransition();
        fadeIn.setNode(popup);
        fadeIn.setDuration(transitionDuration);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        final PauseTransition pause = new PauseTransition();
        pause.setDuration(pauseDuration);

        final FadeTransition fadeOut = new FadeTransition();
        fadeOut.setNode(popup);
        fadeOut.setDuration(transitionDuration);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        final SequentialTransition transition = new SequentialTransition();
        transition.getChildren().addAll(fadeIn, pause, fadeOut);
        transition.setOnFinished(event -> removeScene(popup));

        addScene(popup);
        transition.play();
    }

    public static boolean addScene(Node node) {
        if (node == null)
            return false;
        return SceneManager.sceneStack.getChildren().add(node);
    }

    public static boolean addScene(String fxmlName) {
        final Node fxmlData = loadFXML(fxmlName);
        if (fxmlData == null)
            return false;

        return addScene(fxmlData);
    }

    public static boolean addAllScenes(Node... nodes) {
        if (Arrays.asList(nodes).contains(null))
            return false;

        return SceneManager.sceneStack.getChildren().addAll(nodes);
    }

    public static void insertScene(int index, Node node) {
        if (index < 0 || index > SceneManager.sceneStack.getChildren().size()
                || node == null)
            return;

        SceneManager.sceneStack.getChildren().add(index, node);
    }

    public static void insertScene(int index, String fxmlName) {
        if (index < 0 || index > SceneManager.sceneStack.getChildren().size())
            return;

        final Node fxmlData = loadFXML(fxmlName);
        if (fxmlData == null)
            return;

        insertScene(index, fxmlData);
    }

    public static boolean insertAllScenes(int index, Node... nodes) {
        if (index < 0 || index > SceneManager.sceneStack.getChildren().size()
                || Arrays.asList(nodes).contains(null))
            return false;
        return SceneManager.sceneStack.getChildren().addAll(index, List.of(nodes));
    }

    public static boolean removeScene(Object o) {
        if (o == null)
            return false;
        return SceneManager.sceneStack.getChildren().remove(o);
    }

    public static Node removeScene(int index) {
        if (index < 0 || index >= SceneManager.sceneStack.getChildren().size())
            return null;
        return SceneManager.sceneStack.getChildren().remove(index);
    }

    public static Stage getStage() {
        return SceneManager.stage;
    }

    public static Scene getScene() {
        return SceneManager.scene;
    }

    public static Color getBackgroundColor() {
        return SceneManager.backgroundColor;
    }

    public static void setBackgroundColor(Color color) {
        SceneManager.backgroundColor = color;
    }

}
