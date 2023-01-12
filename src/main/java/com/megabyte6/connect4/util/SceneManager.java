package com.megabyte6.connect4.util;

import java.io.IOException;

import com.megabyte6.connect4.controller.Controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneManager {

    private static final String RESOURCE_PATH = "/com/megabyte6/connect4/view/";

    private static Stage stage;
    private static Scene scene;
    private static StackPane root;
    private static Color backgroundColor;

    private SceneManager() {
    }

    public static void init(Stage stage, Scene scene) {
        init(stage, scene, Color.WHITE);
    }

    public static void init(Stage stage, Scene scene, Color backgroundColor) {
        SceneManager.stage = stage;
        SceneManager.scene = scene;
        SceneManager.backgroundColor = backgroundColor;
    }

    public static void switchScenes(String fxmlName, Duration totalTransitionDuration) {
        // Get old scene.
        final Scene oldScene = SceneManager.scene;
        final StackPane oldRoot = SceneManager.root;
        oldScene.setFill(SceneManager.backgroundColor);

        // Load new scene.
        final String path = RESOURCE_PATH + fxmlName + ".fxml";
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource(path));

            SceneManager.root = new StackPane(fxmlLoader.load());
            SceneManager.scene = new Scene(root);
            SceneManager.scene.setFill(SceneManager.backgroundColor);
            SceneManager.root.setOpacity(0);

            Controller controller = fxmlLoader.getController();
            controller.initialize();
        } catch (IOException e) {
            System.err.println("ERROR: Cannot read file '" + path + "'");
            e.printStackTrace();
            return;
        }

        // Set up fade transitions.
        final FadeTransition oldSceneFadeOut = new FadeTransition(
                totalTransitionDuration.divide(2), oldRoot);
        oldSceneFadeOut.setFromValue(1);
        oldSceneFadeOut.setToValue(0);

        final FadeTransition newSceneFadeIn = new FadeTransition(
                totalTransitionDuration.divide(2), SceneManager.root);
        newSceneFadeIn.setFromValue(0);
        newSceneFadeIn.setToValue(1);

        // Play transitions.
        oldSceneFadeOut.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // Swap scenes between transitions.
                SceneManager.stage.setScene(SceneManager.scene);
                // Start the new scene's fade-in animation.
                newSceneFadeIn.play();
            };
        });
        oldSceneFadeOut.play();
    }

    // public static Pair<Node, Controller> loadFXML(String fxmlFileName) {
    //     Node node;
    //     Controller controller;

    //     final String path = RESOURCE_PATH + fxmlFileName;
    //     try {
    //         final FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource(path));
    //         node = fxmlLoader.load();
    //         controller = fxmlLoader.getController();
    //     } catch (IOException e) {
    //         System.err.println("ERROR: Cannot read file '" + path + "'");
    //         e.printStackTrace();
    //         return Tuple.of(null, null);
    //     }
    //     return Tuple.of(node, controller);
    // }

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
