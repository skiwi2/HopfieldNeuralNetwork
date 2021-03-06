package com.github.skiwi2.hopfieldnn;

import com.github.skiwi2.hopfieldnn.neuralnetwork.NeuralNetwork;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Frank van Heeswijk
 */
public class MainController implements Initializable {
    @FXML
    private GridPane resultGridPane;

    @FXML
    private HBox patternHBox;

    @FXML
    private TextField stepDelayTextField;

    public static final int GRID_COUNT = 10;

    private final NeuralNetwork neuralNetwork = new NeuralNetwork(GRID_COUNT * GRID_COUNT);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    });

    private final Grid2D<CellPane> gridCells = new Grid2D<>(GRID_COUNT, GRID_COUNT);

    private final Map<Integer, Grid2D<Boolean>> patternGrids = new HashMap<>();

    private Grid2D<Boolean> currentGrid = new Grid2D<>(GRID_COUNT, GRID_COUNT);
    private int patternCount = 1;

    public MainController() {
        currentGrid.setAll(false);
        this.patternGrids.put(0, currentGrid);
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        for (int i = 0; i < GRID_COUNT; i++) {
            for (int j = 0; j < GRID_COUNT; j++) {
                CellPane cellPane = new CellPane(currentGrid, i, j);
                cellPane.initialize(null, null);
                gridCells.set(i, j, cellPane);
                resultGridPane.add(cellPane, i, j);
            }
        }

        Label resolveLabel = new Label("Recall");
        resolveLabel.setOnMouseClicked(mouseEvent -> setGridForPatternNumber(0));
        patternHBox.getChildren().add(resolveLabel);

        stepDelayTextField.setText("0");
    }

    @FXML
    private void onAddPatternAction(final ActionEvent actionEvent) {
        Label label = new Label("Pattern " + patternCount);
        label.setOnMouseClicked(mouseEvent -> setGridForPatternNumber(Integer.parseInt(label.getText().replace("Pattern ", ""))));
        currentGrid = new Grid2D<>(GRID_COUNT, GRID_COUNT);
        currentGrid.setAll(false);
        patternGrids.put(patternCount, currentGrid);
        setGridForPatternNumber(patternCount);
        patternHBox.getChildren().add(label);
        patternCount++;
    }

    private void setGridForPatternNumber(final int patternNumber) {
        setGridForPattern(patternGrids.get(patternNumber));
    }

    private void setGridForPattern(final Grid2D<Boolean> pattern) {
        currentGrid = pattern;
        gridCells.forEach(cellPane -> cellPane.setGrid(currentGrid));
    }

    @FXML
    private void onLearnAction(final ActionEvent actionEvent) {
        Set<Grid2D<Integer>> patterns = patternGrids.entrySet().stream()
            .filter(entry -> entry.getKey() >= 1)
            .map(Map.Entry::getValue)
            .map(grid -> grid.map(bool -> bool ? 1 : -1))
            .collect(Collectors.toSet());
        neuralNetwork.learn(patterns);
    }

    @FXML
    private void onRecallAction(final ActionEvent actionEvent) {
        int stepDelay = Integer.parseInt(stepDelayTextField.getText());
        executorService.submit(() -> {
            neuralNetwork.recall(currentGrid.map(bool -> bool ? 1 : -1), intermediatePattern -> {
                try {
                    Thread.sleep(stepDelay);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                currentGrid = intermediatePattern.map(intValue -> (intValue == 1));
                Platform.runLater(() -> setGridForPattern(currentGrid));
            });
        });
    }
}
