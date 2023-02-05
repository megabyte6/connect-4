package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import static com.megabyte6.connect4.util.Range.range;

public class SettingsController implements Controller {

    private Runnable runAfter = () -> {
    };

    @FXML
    private AnchorPane root;

    @FXML
    private TextField columnCount;
    @FXML
    private TextField rowCount;
    @FXML
    private ComboBox<Integer> winningLength;

    @FXML
    private ColorPicker player1Color;
    @FXML
    private ColorPicker player2Color;

    @FXML
    private void initialize() {
        columnCount.setText(App.getColumnCount() + "");
        rowCount.setText(App.getRowCount() + "");

        updateGoalLengthOptions();

        player1Color.setValue(App.getPlayer1().getColor());
        player2Color.setValue(App.getPlayer2().getColor());

        columnCount.setOnKeyTyped(event -> {
            if (inputValid()) {
                updateGoalLengthOptions();
                updateAppSettings();
            }
        });
        rowCount.setOnKeyTyped(event -> {
            if (inputValid()) {
                updateGoalLengthOptions();
                updateAppSettings();
            }
        });
        winningLength.setOnAction(event -> {
            if (inputValid())
                updateAppSettings();
        });
        player1Color.setOnAction(event -> {
            if (inputValid())
                updateAppSettings();
        });
        player2Color.setOnAction(event -> {
            if (inputValid())
                updateAppSettings();
        });
    }

    private boolean inputValid() {
        int selectedColumnCount, selectedRowCount;

        columnCount.setStyle("-fx-text-fill: white;"
                + "-fx-background-color: #3d3d3d;");
        try {
            selectedColumnCount = Integer.parseInt(columnCount.getText());
        } catch (NumberFormatException e) {
            columnCount.setStyle(columnCount.getStyle() + "-fx-border-color: red;");
            return false;
        }

        rowCount.setStyle("-fx-text-fill: white;"
                + "-fx-background-color: #3d3d3d;");
        try {
            selectedRowCount = Integer.parseInt(rowCount.getText());
        } catch (NumberFormatException e) {
            rowCount.setStyle(rowCount.getStyle() + "-fx-border-color: red;");
            return false;
        }

        winningLength.setStyle("-fx-background-color: #3d3d3d;");
        int selectedGoalLength = winningLength.getSelectionModel().getSelectedIndex() + 1;
        if (selectedGoalLength > Math.min(selectedColumnCount, selectedRowCount)) {
            winningLength.setStyle(winningLength.getStyle() + "-fx-border-color: red;");
            return false;
        }

        return true;
    }

    private void updateGoalLengthOptions() {
        winningLength.getItems().clear();
        int maxGoalLengthOption = Math.min(App.getColumnCount(), App.getRowCount());
        for (int i : range(maxGoalLengthOption))
            winningLength.getItems().add(i + 1);

        winningLength.setValue(App.getWinRequirement());
    }

    private void updateAppSettings() {
        App.setColumnCount(Integer.parseInt(columnCount.getText()));
        App.setRowCount(Integer.parseInt(rowCount.getText()));
        App.setWinRequirement(winningLength.getSelectionModel().getSelectedIndex() + 1);
        App.getPlayer1().setColor(player1Color.getValue());
        App.getPlayer2().setColor(player2Color.getValue());
    }

    public void setOnClosed(Runnable run) {
        runAfter = run;
    }

    @FXML
    private void handleCloseButton() {
        if (!inputValid()) {
            SceneManager.popup("Fix the fields highlighted in red.");
            return;
        }

        SceneManager.removeTopScene();

        App.setColumnCount(Integer.parseInt(columnCount.getText()));
        App.setRowCount(Integer.parseInt(rowCount.getText()));
        App.setWinRequirement(winningLength.getSelectionModel().getSelectedItem());
        App.getPlayer1().setColor(player1Color.getValue());
        App.getPlayer2().setColor(player2Color.getValue());

        runAfter.run();
    }

    @Override
    public void setDisable(boolean disabled) {
        root.setDisable(disabled);
        root.setOpacity(disabled ? App.DISABLED_OPACITY : 1);
    }
}
