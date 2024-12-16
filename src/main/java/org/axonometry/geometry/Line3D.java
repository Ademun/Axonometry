package org.axonometry.geometry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Line3D implements GeometricalObject, Drawable, Transformable, Projectable, Selectable {
    private final String name;
    private final double length;
    private final Point3D[] points;
    private final Vector3D directionVector;
    private boolean isProjecting = false;
    private boolean isSelected = false;

    public Line3D(Point3D[] points) {
        if (points.length != 2) {
            throw new IllegalArgumentException("Line3D should be created from 2 points");
        }
        Vector3D lineVector = new Vector3D(points[0], points[1]);
        this.name = Arrays.stream(points).map(point -> point.getName()).collect(Collectors.joining(""));
        this.length = lineVector.mag();
        this.points = points;
        this.directionVector = lineVector.ort();
    }

    private Line3D(String name, double length, Point3D[] points, Vector3D directionVector, boolean isProjecting, boolean isSelected) {
        this.name = name;
        this.length = length;
        this.points = points;
        this.directionVector = directionVector;
        this.isProjecting = isProjecting;
        this.isSelected = isSelected;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        drawLine(gc);
        if (isProjecting) {
            drawProjections(gc);
        }
        gc.restore();
        Arrays.stream(points).forEach(point -> point.draw(gc));
    }

    private void drawLine(GraphicsContext gc) {
        gc.setStroke(Color.rgb(100, 255, 200));
        if (isSelected) {
            gc.setStroke(Color.ORANGE);
        }
        gc.setLineWidth(2);
        gc.strokeLine(
                CoordinateSystem.transformForDrawing(points[0].getCoordinates()).x,
                CoordinateSystem.transformForDrawing(points[0].getCoordinates()).z,
                CoordinateSystem.transformForDrawing(points[1].getCoordinates()).x,
                CoordinateSystem.transformForDrawing(points[1].getCoordinates()).z
        );
    }

    @Override
    public void drawProjections(GraphicsContext gc) {
        gc.save();
        gc.setStroke(Color.rgb(100, 255, 200, 0.5));
        Projection[] projectionsA = points[0].getProjections();
        Projection[] projectionsB = points[1].getProjections();
        IntStream.range(0, projectionsA.length).forEach(i -> {
            gc.strokeLine(
                    CoordinateSystem.transformForDrawing(projectionsA[i].getCoordinates()).x,
                    CoordinateSystem.transformForDrawing(projectionsA[i].getCoordinates()).z,
                    CoordinateSystem.transformForDrawing(projectionsB[i].getCoordinates()).x,
                    CoordinateSystem.transformForDrawing(projectionsB[i].getCoordinates()).z
            );
            projectionsA[i].draw(gc);
            projectionsB[i].draw(gc);
        });
        gc.restore();
    }

    @Override
    public GeometricalObject transform(double rx, double ry, double rz, double scale) {
        Point3D[] transformedPoints = Arrays.stream(points).map(point -> point.transform(rx, ry, rz, scale)).toArray(Point3D[]::new);
        return new Line3D(name, length, transformedPoints, directionVector, isProjecting, isSelected);
    }

    @Override
    public boolean isClicked(double x, double z) {
        Point3D point = new Point3D("Temp", new Vector3D(new double[][]{{x}, {0}, {z}}));
        Point3D A = new Point3D("A", new Vector3D(new double[][]{
                {CoordinateSystem.transformForDrawing(points[0].getCoordinates()).x},
                {0},
                {CoordinateSystem.transformForDrawing(points[0].getCoordinates()).z}
        }));
        Point3D B = new Point3D("B", new Vector3D(new double[][]{
                {CoordinateSystem.transformForDrawing(points[1].getCoordinates()).x},
                {0},
                {CoordinateSystem.transformForDrawing(points[1].getCoordinates()).z}
        }));
        Vector3D temp = new Vector3D(A, point);
        Vector3D dirVector = new Vector3D(A, B);
        double distance = Vector3D.vectorProduct(temp, dirVector).mag() / dirVector.mag();
        return distance <= 6;
    }

    @Override
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    @Override
    public boolean getSelected() {
        return isSelected;
    }

    @Override
    public Point3D[] getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }
    public boolean isProjecting() {
        return isProjecting;
    }

    public void setProjecting(boolean projecting) {
        isProjecting = projecting;
    }

    public Vector3D getDirectionVector() {
        return directionVector;
    }
}