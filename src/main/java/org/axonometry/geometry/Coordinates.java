package org.axonometry.geometry;

public class Coordinates extends Matrix {
    public Coordinates(double[][] data) {
        super(data);
        if (data.length > 3 | data[0].length > 1) {
            throw new IllegalArgumentException("Coordinates can have only 3 values");
        }
    }

    public double getX() {
        return getData()[0][0];
    }

    public double getY() {
        return getData()[1][0];
    }

    public double getZ() {
        return getData()[2][0];
    }
}
