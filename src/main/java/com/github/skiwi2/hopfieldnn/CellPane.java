package com.github.skiwi2.hopfieldnn;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Frank van Heeswijk
 */
public class CellPane extends AnchorPane implements Initializable {
    private final BooleanProperty filledProperty = new SimpleBooleanProperty();
    private final int row;
    private final int column;

    private Grid2D<Boolean> grid;

    public CellPane(final Grid2D<Boolean> grid, final int row, final int column) {
        this.grid = grid;
        this.row = row;
        this.column = column;
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resourceBundle) {
        filledProperty.addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                setStyle("-fx-border-color: black; -fx-background-color: black");
            } else {
                setStyle("-fx-border-color: black; -fx-background-color: white");
            }
            grid.set(row, column, newValue);
        });
        filledProperty.set(true);
        filledProperty.set(false);
        setOnMousePressed(mouseEvent -> filledProperty.set(!filledProperty.get()));
        setOnMouseEntered(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown() || mouseEvent.isControlDown()) {
                filledProperty.set(!filledProperty.get());
            }
        });
    }

    public void setGrid(final Grid2D<Boolean> grid) {
        this.grid = grid;
        filledProperty.set(grid.get(row, column));
    }
}
