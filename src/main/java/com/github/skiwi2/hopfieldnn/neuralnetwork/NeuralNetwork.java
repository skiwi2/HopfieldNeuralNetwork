package com.github.skiwi2.hopfieldnn.neuralnetwork;

import com.github.skiwi2.hopfieldnn.Grid2D;

import java.util.Collection;

/**
 * @author Frank van Heeswijk
 */
public class NeuralNetwork {
    private final int neuronCount;

    private Grid2D<Float> weightMatrix;

    public NeuralNetwork(final int neuronCount) {
        this.neuronCount = neuronCount;
    }

    public void learn(final Collection<Grid2D<Integer>> patterns) {
        weightMatrix = new Grid2D<>(neuronCount, neuronCount);

        for (int i = 0; i < neuronCount; i++) {
            for (int j = 0; j < neuronCount; j++) {
                float weight = 0f;
                for (Grid2D<Integer> pattern : patterns) {
                    weight += (pattern.get(i) * pattern.get(j));
                }
                weight /= neuronCount;
                weightMatrix.set(i, j, weight);
            }
        }
    }


}
