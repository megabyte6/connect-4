package com.megabyte6.connect4.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.megabyte6.connect4.controller.Controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneManager {

    private static final String RESOURCE_PATH = "/com/megabyte6/connect4/view/";

    private static Stage stage;
    private static Scene scene;
    private static StackPane sceneStack;
    private static Color backgroundColor;

    private static HashMap<String, Integer> sceneStackNames = new HashMap<>();

    private SceneManager() {
    }

    public static void init(Stage stage, String fxmlName) {
        init(stage, fxmlName, Color.WHITE);
    }

    public static void init(Stage stage, String fxmlName, Color backgroundColor) {
        SceneManager.stage = stage;

        final Node fxmlData = loadFXML(fxmlName);
        SceneManager.sceneStack = new StackPane();
        SceneManager.sceneStack.getChildren().add(fxmlData);
        SceneManager.sceneStackNames.put(fxmlName, 0);
        SceneManager.scene = new Scene(SceneManager.sceneStack);

        SceneManager.backgroundColor = backgroundColor;
    }

    private static Node loadFXML(String fxmlName) {
        final String path = RESOURCE_PATH + fxmlName + ".fxml";
        final Node root;
        final Controller controller;

        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource(path));

            root = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            System.err.println("ERROR: Cannot read file '" + path + "'");
            e.printStackTrace();
            return null;
        }

        controller.initialize();

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

        final HashMap<String, Integer> newSceneStackNames = new HashMap<>();
        newSceneStackNames.put(fxmlName, 0);

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
            SceneManager.stage.setScene(newScene);
            SceneManager.sceneStackNames = newSceneStackNames;
            // Start the new scene's fade-in animation.
            newSceneFadeIn.play();
        });
        oldSceneFadeOut.play();
    }

    public static void addScene(String fxmlName) {
        final Node fxmlData = loadFXML(fxmlName);
        addScene(fxmlData);
    }

    public static void addScene(Node node) {
        SceneManager.sceneStack.getChildren().add(node);
    }

    public static void addAllScenes(Node... nodes) {
        SceneManager.sceneStack.getChildren().addAll(nodes);
    }

    public static void insertScene(int index, String fxmlName) {
        final Node fxmlData = loadFXML(fxmlName);
        insertScene(index, fxmlData);
    }

    public static void insertScene(int index, Node node) {
        SceneManager.sceneStack.getChildren().add(index, node);
    }

    public static void insertAllScenes(int index, Node... nodes) {
        SceneManager.sceneStack.getChildren().addAll(index, List.of(nodes));
    }

    public static Node removeScene(int index) {
        if (index < 0 || index >= SceneManager.sceneStack.getChildren().size())
            return null;

        return SceneManager.sceneStack.getChildren().remove(index);
    }

    public static Node removeScene(String name) {
        if (!SceneManager.sceneStackNames.containsKey(name))
            return null;

        return removeScene(SceneManager.sceneStackNames.get(name));
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
