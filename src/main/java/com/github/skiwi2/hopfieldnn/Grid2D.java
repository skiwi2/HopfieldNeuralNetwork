package com.github.skiwi2.hopfieldnn;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author Frank van Heeswijk
 */
public class Grid2D<T> {
    private final int width;
    private final int height;

    private final T[] data;

    public Grid2D(final int width, final int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("Illegal width: width = " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Illegal height: height = " + width);
        }
        this.width = width;
        this.height = height;
        this.data = (T[])new Object[width * height];
    }

    public void set(final int row, final int column, final T element) {
        if (row < 0 || row >= width) {
            throw new IllegalArgumentException("Illegal row: row = " + row);
        }
        if (column < 0 || column >= height) {
            throw new IllegalArgumentException("Illegal column: column = " + column);
        }
        data[row * width + column] = element;
    }

    public T get(final int row, final int column) {
        if (row < 0 || row >= width) {
            throw new IllegalArgumentException("Illegal row: row = " + row);
        }
        if (column < 0 || column >= height) {
            throw new IllegalArgumentException("Illegal column: column = " + column);
        }
        return data[row * width + column];
    }

    public void setAll(final T element) {
        for (int row = 0; row < width; row++) {
            for (int column = 0; column < height; column++) {
                set(row, column, element);
            }
        }
    }

    public void forEach(final Consumer<T> consumer) {
        for (int row = 0; row < width; row++) {
            for (int column = 0; column < height; column++) {
                consumer.accept(get(row, column));
            }
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Grid2D grid2D = (Grid2D)obj;

        if (height != grid2D.height) return false;
        if (width != grid2D.width) return false;
        if (!Arrays.equals(data, grid2D.data)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
