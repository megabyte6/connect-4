package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import static com.megabyte6.connect4.util.Range.range;

public class SettingsController implements Controller {

    private Runnable runAfter = () -> {
    };

    @FXML
    private TextField columnCount;
    @FXML
    private TextField rowCount;
    @FXML
    private ChoiceBox<Integer> goalLength;

    @FXML
    private ColorPicker player1Color;
    @FXML
    private ColorPicker player2Color;

    @FXML
    private void initialize() {
        columnCount.setText(App.getColumnCount() + "");
        rowCount.setText(App.getRowCount() + "");

        for (int i : range(1, App.getWinRequirement()))
            goalLength.getItems().add(i);

        player1Color.setValue(App.getPlayer1().getColor());
        player2Color.setValue(App.getPlayer2().getColor());

        columnCount.setOnAction(event -> inputValid());
        rowCount.setOnAction(event -> inputValid());
        goalLength.setOnAction(event -> inputValid());
        player1Color.setOnAction(event -> inputValid());
        player2Color.setOnAction(event -> inputValid());
    }

    private boolean inputValid() {
        int selectedColumnCount, selectedRowCount;
        try {
            selectedColumnCount = Integer.parseInt(columnCount.getText());
            columnCount.setStyle("-fx-text-fill: white;"
                    + "-fx-background-color: #3d3d3d;");
        } catch (NumberFormatException e) {
            columnCount.setStyle("-fx-text-fill: white;"
                    + "-fx-background-color: #3d3d3d;"
                    + "-fx-border-color: red;");
            return false;
        }
        try {
            selectedRowCount = Integer.parseInt(rowCount.getText());
            rowCount.setStyle("-fx-text-fill: white;"
                    + "-fx-background-color: #3d3d3d;");
        } catch (NumberFormatException e) {
            rowCount.setStyle("-fx-text-fill: white;"
                    + "-fx-background-color: #3d3d3d;"
                    + "-fx-border-color: red;");
            return false;
        }

        int selectedGoalLength = goalLength.getSelectionModel().getSelectedItem();
        if (selectedGoalLength > Math.min(selectedColumnCount, selectedRowCount)) {
            goalLength.setStyle("-fx-background-color: #3d3d3d;"
                    + "-fx-border-color: red;");
            return false;
        }
        goalLength.setStyle("-fx-background-color: #3d3d3d;");

        return true;
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
        App.setWinRequirement(goalLength.getSelectionModel().getSelectedItem());
        App.getPlayer1().setColor(player1Color.getValue());
        App.getPlayer2().setColor(player2Color.getValue());

        runAfter.run();
    }

    @Override
    public void setDisable(boolean disabled) {
    }
}
