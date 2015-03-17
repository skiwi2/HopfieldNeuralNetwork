package com.github.skiwi2.hopfieldnn;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Frank van Heeswijk
 */
public class MainController implements Initializable {
    @FXML
    private GridPane resultGridPane;

    private static final int GRID_COUNT = 10;

    @Override
    public void initialize(final URL location, final ResourceBundle resourceBundle) {
        for (int i = 0; i < GRID_COUNT; i++) {
            for (int j = 0; j < GRID_COUNT; j++) {
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setStyle("-fx-border-color: black");
                resultGridPane.add(anchorPane, i, j);
            }
        }
    }
}
