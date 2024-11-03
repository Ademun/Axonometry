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
        if (rows != matrix.getRows() || cols != matrix.getCols()) {
            throw new IllegalArgumentException("Matrices can't be summed");
        }
        double[][] newData = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newData[i][j] = data[i][j] + matrix.getData()[i][j];
            }
        }
        return new Matrix(newData);
    }

    public Matrix sub(Matrix matrix) {
        if (rows != matrix.getRows() || cols != matrix.getCols()) {
            throw new IllegalArgumentException("Matrices can't be summed");
        } else {
            double[][] newData = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    newData[i][j] = data[i][j] - matrix.getData()[i][j];
                }
            }
            return new Matrix(newData);
        }
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
                {cosTheta + vec.getX() * vec.getX() * (1 - cosTheta),
                        vec.getX() * vec.getY() * (1 - cosTheta) - vec.getZ() * sinTheta,
                        vec.getX() * vec.getZ() * (1 - cosTheta) + vec.getY() * sinTheta
                },
                {
                        vec.getY() * vec.getX() * (1 - cosTheta) + vec.getZ() * sinTheta,
                        cosTheta + vec.getY() * vec.getY() * (1 - cosTheta),
                        vec.getY() * vec.getZ() * (1 - cosTheta) - vec.getX() * sinTheta
                },
                {
                        vec.getZ() * vec.getX() * (1 - cosTheta) - vec.getY() * sinTheta,
                        vec.getZ() * vec.getY() * (1 - cosTheta) + vec.getX() * sinTheta,
                        cosTheta + vec.getZ() * vec.getZ() * (1 - cosTheta)
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


