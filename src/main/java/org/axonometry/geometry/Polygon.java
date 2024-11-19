package org.axonometry.geometry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.axonometry.geometry.CoordinateSystem.transformForDrawing;

public class Polygon implements GeometricalObject {
    private final Point3D[] points;
    private final Color color;
    public Polygon(Point3D[] points, Color color) {
//        if (points.length != 3) throw new IllegalArgumentException("Polygon should consist of only 3 points");
        Vector3D[] vectors = new Vector3D[points.length - 1];
        IntStream.range(1, points.length)
                .forEach(i -> vectors[i - 1] = new Vector3D(points[0], points[i]));
        this.points = points;
        this.color = color;
    }

    public Polygon transform(double rx, double ry, double rz, double scale) {
        Point3D[] transformed = Arrays.stream(points)
                .map(point -> point.transform(rx, ry, rz, scale))
                .toArray(Point3D[]::new);
        return new Polygon(transformed, color);
    }

    public void draw(GraphicsContext gc) {
        double[] xPoints = new double[points.length];
        double[] yPoints = new double[points.length];
        IntStream.range(0, points.length).forEach(i -> {
            points[i].draw(gc);
            xPoints[i] = transformForDrawing(points[i].getCoordinates()).x;
            yPoints[i] = transformForDrawing(points[i].getCoordinates()).z;
        });
        gc.setLineDashes(10);
        gc.setStroke(Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0.9));
        gc.strokePolygon(xPoints, yPoints, points.length);
        gc.setLineDashes(0);
        gc.setFill(color);
        gc.fillPolygon(xPoints, yPoints, points.length);
    }

    public String toString() {
        return String.format("Polygon: %s",
                Arrays.stream(points)
                .map(Point3D::getName)
                .collect(Collectors.joining("")));
    }

    public Point3D[] getPoints() {
        return points;
    }
}
