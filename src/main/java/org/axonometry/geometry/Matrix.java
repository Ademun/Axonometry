package org.axonometry.geometry;

public class Matrix {
    private final int rows;
    private final int cols;
    private final double[][] data;

    public Matrix(double[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix add(Matrix matrix) {
        if (rows != matrix.getRows() || cols != matrix.getCols()) throw new IllegalArgumentException("Matrices can't be summed");
        double[][] newData = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newData[i][j] = data[i][j] + matrix.getData()[i][j];
            }
        }
        return new Matrix(newData);
    }

    public Matrix sub(Matrix matrix) {
        if (rows != matrix.getRows() || cols != matrix.getCols()) throw new IllegalArgumentException("Matrices can't be summed");
        double[][] newData = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newData[i][j] = data[i][j] - matrix.getData()[i][j];
            }
        }
        return new Matrix(newData);
    }

    public Matrix multi(double n) {
        double[][] newData = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newData[i][j] = data[i][j] * n;
            }
        }
        return new Matrix(newData);
    }

    public Matrix multi(Matrix matrix) {
        if (cols != matrix.getRows()) throw new IllegalArgumentException("Matrices cannot be multiplied");
        double[][] newData = new double[rows][matrix.getCols()];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                for (int k = 0; k < matrix.getRows(); k++)
                    newData[i][j] += data[i][k] * matrix.getData()[k][j];
            }
        }
        return new Matrix(newData);
    }

    public Matrix div(double n) {
        double[][] newData = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newData[i][j] = data[i][j] / n;
            }
        }
        return new Matrix(newData);
    }

    public Matrix transpose() {
        double[][] newData = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newData[i][j] = data[j][i];
            }
        }
        return new Matrix(newData);
    }

    public static Matrix getRotationalMatrix(Vector3D vec, double rad) {
        vec = vec.ort();
        double cosTheta = Math.cos(rad);
        double sinTheta = Math.sin(rad);
        return new Matrix(new double[][]{
                {cosTheta + vec.x * vec.x * (1 - cosTheta),
                        vec.x * vec.y * (1 - cosTheta) - vec.z * sinTheta,
                        vec.x * vec.z * (1 - cosTheta) + vec.y * sinTheta
                },
                {
                        vec.y * vec.x * (1 - cosTheta) + vec.z * sinTheta,
                        cosTheta + vec.y * vec.y * (1 - cosTheta),
                        vec.y * vec.z * (1 - cosTheta) - vec.x * sinTheta
                },
                {
                        vec.z * vec.x * (1 - cosTheta) - vec.y * sinTheta,
                        vec.z * vec.y * (1 - cosTheta) + vec.x * sinTheta,
                        cosTheta + vec.z * vec.z * (1 - cosTheta)
                }
        });
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double[][] getData() {
        return data;
    }
}


