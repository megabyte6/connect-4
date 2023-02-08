package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.controller.dialog.ConfirmController;
import com.megabyte6.connect4.model.Settings;
import com.megabyte6.connect4.util.SceneManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
    private ColorPicker player1Color;
    @FXML
    private ColorPicker player2Color;

    @FXML
    private void initialize() {
        columnCount.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        columnCount.editorProperty().get().setAlignment(Pos.CENTER);
        final SpinnerValueFactory<Integer> columnValues = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                3,
                Integer.MAX_VALUE,
                App.getSettings().getColumnCount());
        columnCount.setValueFactory(columnValues);

        rowCount.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        rowCount.editorProperty().get().setAlignment(Pos.CENTER);
        final SpinnerValueFactory<Integer> rowValues = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                3,
                Integer.MAX_VALUE,
                App.getSettings().getRowCount());
        rowCount.setValueFactory(rowValues);

        winningLength.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        winningLength.editorProperty().get().setAlignment(Pos.CENTER);
        updateMaxWinningLength();

        player1Color.setValue(App.getPlayer1().getColor());
        player2Color.setValue(App.getPlayer2().getColor());

        columnCount.setOnMouseReleased(event -> {
            updateAppSettings();
            updateMaxWinningLength();
        });
        rowCount.setOnMouseReleased(event -> {
            updateAppSettings();
            updateMaxWinningLength();
        });
        winningLength.setOnMouseReleased(event -> updateAppSettings());
        player1Color.setOnAction(event -> updateAppSettings());
        player2Color.setOnAction(event -> updateAppSettings());
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

        if (maxWinningLength < App.getSettings().getWinRequirement()) {
            winningLength.getValueFactory().setValue(maxWinningLength);
            updateAppSettings();
        }
    }

    private void updateAppSettings() {
        App.getSettings().setColumnCount(columnCount.getValue());
        App.getSettings().setRowCount(rowCount.getValue());
        App.getSettings().setWinRequirement(winningLength.getValue());
        App.getSettings().setPlayer1Color(player1Color.getValue());
        App.getSettings().setPlayer2Color(player2Color.getValue());
    }

    private void resetAllSettings() {
        App.setSettings(Settings.DEFAULT);

        columnCount.getValueFactory().setValue(App.getSettings().getColumnCount());
        rowCount.getValueFactory().setValue(App.getSettings().getRowCount());
        updateMaxWinningLength();
        player1Color.setValue(App.getSettings().getPlayer1Color());
        player2Color.setValue(App.getSettings().getPlayer2Color());
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
