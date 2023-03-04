package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.controller.dialog.ConfirmController;
import com.megabyte6.connect4.model.Settings;
import com.megabyte6.connect4.util.SceneManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;

public class SettingsController implements Controller {

    private Runnable runAfter = () -> {
    };

    @FXML
    private AnchorPane root;

    @FXML
    private Spinner<Integer> columnCount;
    @FXML
    private Spinner<Integer> rowCount;
    @FXML
    private Spinner<Integer> winningLength;

    @FXML
    private CheckBox timerEnabled;
    @FXML
    private Spinner<Integer> timerLength;
    @FXML
    private CheckBox timerAutoDrop;

    @FXML
    private ColorPicker player1Color;
    @FXML
    private ColorPicker player2Color;

    @FXML
    private CheckBox obstaclesEnabled;
    @FXML
    private Spinner<Integer> numOfObstacles;
    @FXML
    private ColorPicker obstacleColor;

    @FXML
    private void initialize() {
        columnCount.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        columnCount.editorProperty().get().setAlignment(Pos.CENTER);

        rowCount.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        rowCount.editorProperty().get().setAlignment(Pos.CENTER);

        winningLength.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        winningLength.editorProperty().get().setAlignment(Pos.CENTER);

        timerLength.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        timerLength.editorProperty().get().setAlignment(Pos.CENTER);

        numOfObstacles.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        numOfObstacles.editorProperty().get().setAlignment(Pos.CENTER);

        reloadSettings();

        // Listeners.
        columnCount.setOnMouseReleased(event -> {
            updateAppSettings();
            updateMaxWinningLength();
            updateMaxObstacles();
        });
        rowCount.setOnMouseReleased(event -> {
            updateAppSettings();
            updateMaxWinningLength();
            updateMaxObstacles();
        });
        winningLength.setOnMouseReleased(event -> updateAppSettings());
        timerEnabled.setOnAction(event -> {
            updateAppSettings();
            timerLength.setDisable(!timerEnabled.isSelected());
            timerAutoDrop.setDisable(!timerEnabled.isSelected());
        });
        timerLength.setOnMouseReleased(event -> updateAppSettings());
        timerAutoDrop.setOnAction(event -> updateAppSettings());
        player1Color.setOnAction(event -> updateAppSettings());
        player2Color.setOnAction(event -> updateAppSettings());
        obstaclesEnabled.setOnAction(event -> {
            updateAppSettings();
            numOfObstacles.setDisable(!obstaclesEnabled.isSelected());
            obstacleColor.setDisable(!obstaclesEnabled.isSelected());
        });
        numOfObstacles.setOnMouseReleased(event -> updateAppSettings());
        obstacleColor.setOnAction(event -> updateAppSettings());
    }

    private void updateMaxWinningLength() {
        final int maxWinningLength = Math.min(
                App.getSettings().getColumnCount(),
                App.getSettings().getRowCount());
        final SpinnerValueFactory<Integer> winningLengthValues = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                3,
                maxWinningLength,
                App.getSettings().getWinRequirement());
        winningLength.setValueFactory(winningLengthValues);

        if (App.getSettings().getWinRequirement() > maxWinningLength) {
            winningLength.getValueFactory().setValue(maxWinningLength);
            updateAppSettings();
        }
    }

    private void updateMaxObstacles() {
        final int maxObstacles = (columnCount.getValue() * rowCount.getValue())
                - (winningLength.getValue() * 2);
        final SpinnerValueFactory<Integer> numOfObstaclesValues = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0,
                maxObstacles,
                App.getSettings().getNumOfObstacles());
        numOfObstacles.setValueFactory(numOfObstaclesValues);

        if (App.getSettings().getNumOfObstacles() > maxObstacles) {
            numOfObstacles.getValueFactory().setValue(maxObstacles);
            updateAppSettings();
        }
    }

    private void updateAppSettings() {
        final Settings settings = App.getSettings();
        settings.setColumnCount(columnCount.getValue());
        settings.setRowCount(rowCount.getValue());
        settings.setWinRequirement(winningLength.getValue());
        settings.setTimerEnabled(timerEnabled.isSelected());
        settings.setTimerLengthInSeconds(timerLength.getValue());
        settings.setTimerAutoDrop(timerAutoDrop.isSelected());
        settings.setPlayer1Color(player1Color.getValue());
        settings.setPlayer2Color(player2Color.getValue());
        settings.setObstaclesEnabled(obstaclesEnabled.isSelected());
        settings.setNumOfObstacles(numOfObstacles.getValue());
        settings.setObstacleColor(obstacleColor.getValue());
    }

    private void reloadSettings() {
        columnCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                3,
                Integer.MAX_VALUE,
                App.getSettings().getColumnCount()));

        rowCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                3,
                Integer.MAX_VALUE,
                App.getSettings().getRowCount()));

        updateMaxWinningLength();

        timerEnabled.setSelected(App.getSettings().isTimerEnabled());

        timerLength.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1,
                Integer.MAX_VALUE,
                App.getSettings().getTimerLengthInSeconds()));
        timerLength.setDisable(!timerEnabled.isSelected());

        timerAutoDrop.setSelected(App.getSettings().isTimerAutoDrop());
        timerAutoDrop.setDisable(!timerEnabled.isSelected());

        player1Color.setValue(App.getSettings().getPlayer1Color());

        player2Color.setValue(App.getSettings().getPlayer2Color());

        obstaclesEnabled.setSelected(App.getSettings().isObstaclesEnabled());

        final int maxObstacles = (App.getSettings().getColumnCount() * App.getSettings().getRowCount())
                - (App.getSettings().getWinRequirement() * 2);
        numOfObstacles.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0,
                maxObstacles,
                App.getSettings().getNumOfObstacles()));
        numOfObstacles.setDisable(!obstaclesEnabled.isSelected());
        updateMaxObstacles();

        obstacleColor.setValue(App.getSettings().getObstacleColor());
        obstacleColor.setDisable(!obstaclesEnabled.isSelected());
    }

    private void resetAllSettings() {
        App.setSettings(Settings.DEFAULT.get());
        reloadSettings();
    }

    public void setOnClosed(Runnable run) {
        runAfter = run;
    }

    @FXML
    private void handleCloseButton() {
        updateAppSettings();
        App.writeSettings();

        SceneManager.removeTopScene();
        runAfter.run();
    }

    @FXML
    private void handleResetAllButton() {
        setDisable(true);

        final var loadedData = SceneManager.loadFXMLAndController("dialog/Confirm");
        final Node root = loadedData.a();
        final ConfirmController controller = (ConfirmController) loadedData.b();

        controller.setText("Are you sure you want to reset all settings to their defaults?");
        controller.setOnOk(() -> {
            resetAllSettings();
            setDisable(false);
        });
        controller.setOnCancel(() -> setDisable(false));

        SceneManager.addScene(root);
    }

    @Override
    public void setDisable(boolean disabled) {
        root.setDisable(disabled);
        root.setOpacity(disabled ? App.DISABLED_OPACITY : 1);
    }
}
