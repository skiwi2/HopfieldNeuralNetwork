package com.github.skiwi2.hopfieldnn.neuralnetwork;

import com.github.skiwi2.hopfieldnn.Grid2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

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

    public void recall(final Grid2D<Integer> pattern, final Consumer<Grid2D<Integer>> currentPatternConsumer) {
        List<Neuron> neurons = new ArrayList<>();
        for (int i = 0; i < neuronCount; i++) {
            Neuron neuron = new Neuron();
            neuron.setData(pattern.get(i));
            neurons.add(neuron);
        }

        Random random = new Random();
        while (true) {  //TODO check if stable, then break
            int randomNeuronIndex = random.nextInt(neuronCount);

            float sum = 0f;
            for (int j = 0; j < neuronCount; j++) {
                if (j == randomNeuronIndex) {
                    continue;
                }
                sum += (weightMatrix.get(randomNeuronIndex, j) * neurons.get(j).getData());
            }

            int newValue = (sum < 0f) ? -1 : 1;
            neurons.get(randomNeuronIndex).setData(newValue);
            pattern.set(randomNeuronIndex, newValue);
            currentPatternConsumer.accept(pattern);
        }
    }
}
