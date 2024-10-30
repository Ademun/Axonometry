package org.axonometry.geometry;

public class Vector3D extends Matrix {
    private final double x;
    private final double y;
    private final double z;
    public Vector3D(double[][] data) {
        super(data);
        if (data.length > 3) {
            throw new IllegalArgumentException("Vector3D can have only 3 values");
        }
        x = getData()[0][0];
        y = getData()[1][0];
        z = getData()[2][0];
    }
    public Vector3D(Vertex3D a, Vertex3D b) {
        super(new double[][] {
                {b.getCoordinates().getX() - a.getCoordinates().getX()},
                {b.getCoordinates().getY() - a.getCoordinates().getY()},
                {b.getCoordinates().getZ() - a.getCoordinates().getZ()}
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

    public Vector3D multi(Vector3D n) {
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
        return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
    }
    public static Vector3D vectorProduct(Vector3D a, Vector3D b) {
        return new Vector3D(new double[][]{
                {a.getY() * b.getZ() - a.getZ() * b.getY()},
                {a.getZ() * b.getX() - a.getX() * b.getZ()},
                {a.getX() * b.getY() - a.getY() * b.getX()}
        });
    }
    public static double mixedProduct(Vector3D a, Vector3D b, Vector3D c) {
        return scalarProduct(a, vectorProduct(b, c));
    }

    public static boolean areCollinear(Vector3D a, Vector3D b) {
        return vectorProduct(a, b).mag() == 0;
    }
    public static boolean areCoplanar(Vector3D ...vertices) {
        for (int i = 0; i < vertices.length; i++) {
            for (int j = 0; j < vertices.length; j++) {
                if (j == i) continue;
                for (int k = 0; k < vertices.length; k++) {
                    if (k == i || k == j) continue;
                    if (mixedProduct(vertices[i], vertices[j], vertices[k]) != 0) return false;
                }
            }
        }
        return true;
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
