package org.axonometry.geometry;

import java.util.Arrays;

public class Vector3D extends Matrix {
    public final double x;
    public final double y;
    public final double z;
    public Vector3D(double[][] data) {
        super(data);
        if (data.length > 3) throw new IllegalArgumentException("Vector3D can have only 3 values");
        Arrays.stream(data).filter(coordinate -> coordinate.length != 1).forEach(coordinate -> {
            throw new IllegalArgumentException("Invalid data");
        });
        x = getData()[0][0];
        y = getData()[1][0];
        z = getData()[2][0];
    }
    public Vector3D(Vertex3D a, Vertex3D b) {
        super(new double[][] {
                {b.getCoordinates().x - a.getCoordinates().x},
                {b.getCoordinates().y - a.getCoordinates().y},
                {b.getCoordinates().z - a.getCoordinates().z}
        });
        x = getData()[0][0];
        y = getData()[1][0];
        z = getData()[2][0];
    }

    public Vector3D add(Vector3D vec) {
        return new Vector3D(super.add(vec).getData());
    }
    public Vector3D sub(Vector3D vec) {
        return new Vector3D(super.sub(vec).getData());
    }

    public Vector3D multi(double n) {
        return new Vector3D(super.multi(n).getData());
    }
    public Vector3D div(double n) {
        return new Vector3D(super.div(n).getData());
    }
    public double mag() {
        return Math.sqrt(x * x + y * y + z * z);
    }
    public Vector3D ort() {
        return new Vector3D(new double[][]{
                {x / mag()},
                {y / mag()},
                {z / mag()}
        });
    }
    public static double scalarProduct(Vector3D a, Vector3D b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
    public static Vector3D vectorProduct(Vector3D a, Vector3D b) {
        return new Vector3D(new double[][]{
                {a.y * b.z - a.z * b.y},
                {a.z * b.x - a.x * b.z},
                {a.x * b.y - a.y * b.x}
        });
    }
    public static double mixedProduct(Vector3D a, Vector3D b, Vector3D c) {
        return scalarProduct(a, vectorProduct(b, c));
    }

    public static boolean areCollinear(Vector3D a, Vector3D b) {
        return vectorProduct(a, b).mag() == 0;
    }
    public static boolean areCoplanar(Vector3D a, Vector3D b, Vector3D c) {
        return mixedProduct(a, b, c) == 0;
    }
    public static boolean areOrthogonal(Vector3D ...vertices) {
        for (int i = 0; i < vertices.length; i++) {
            for (int j = 0; j < vertices.length; j++) {
                if (j == i) continue;
                if (scalarProduct(vertices[i], vertices[j]) != 0) return false;
            }
        }
        return true;
    }

    public String toString() {
        return String.format("(%f, %f, %f)", x, y, z);
    }
}
