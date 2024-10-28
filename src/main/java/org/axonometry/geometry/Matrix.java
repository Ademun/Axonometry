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
        } else {
            double[][] newData = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    newData[i][j] = data[i][j] + matrix.getData()[i][j];
                }
            }
            return new Matrix(newData);
        }
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

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                System.out.printf("%9.4f ", data[i][j]);
            System.out.println();
        }
    }

    public static Matrix getRotationalMatrix(double deg, String axis) {
        return switch (axis) {
            case "x" -> new Matrix(new double[][]{
                    {1, 0, 0},
                    {0, Math.cos(deg), -1 * Math.sin(deg)},
                    {0, Math.sin(deg), Math.cos(deg)}});
            case "y" -> new Matrix(new double[][]{
                    {Math.cos(deg), 0, Math.sin(deg)},
                    {0, 1, 0},
                    {-1 * Math.sin(deg), 0, Math.cos(deg)}
            });
            case "z" -> new Matrix(new double[][]{
                    {Math.cos(deg), -1 * Math.sin(deg), 0},
                    {Math.sin(deg), Math.cos(deg), 0},
                    {0, 0, 1}
            });
            default -> throw new IllegalArgumentException("Illegal axis");
        };
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


