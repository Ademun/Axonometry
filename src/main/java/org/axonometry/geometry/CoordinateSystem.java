package org.axonometry.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;

import java.util.Arrays;
import java.util.stream.IntStream;

public class CoordinateSystem implements GeometricalObject {
    private final Point3D[] points;

    public CoordinateSystem() {
        this.points = new Point3D[]{
                new Point3D("x", new Vector3D(new double[][]{{1000}, {0}, {0}})),
                new Point3D("y", new Vector3D(new double[][]{{0}, {1000}, {0}})),
                new Point3D("z", new Vector3D(new double[][]{{0}, {0}, {1000}})),
                new Point3D("-x", new Vector3D(new double[][]{{-1000}, {0}, {0}})),
                new Point3D("-y", new Vector3D(new double[][]{{0}, {-1000}, {0}})),
                new Point3D("-z", new Vector3D(new double[][]{{0}, {0}, {-1000}}))};
    }

    public CoordinateSystem(Point3D[] points) {
        this.points = points;
    }

    public CoordinateSystem transform(double rx, double ry, double rz, double scale) {
        Point3D[] transformed = Arrays.stream(points)
                .map(point -> point.transform(rx, ry, rz, scale))
                .toArray(Point3D[]::new);
        return new CoordinateSystem(transformed);
    }

    public void draw(GraphicsContext gc) {
        Color[] axesColors = new Color[]{Color.RED.desaturate(), Color.BLUE.desaturate(), Color.GREEN.desaturate()};
        IntStream.range(0, points.length / 2).forEach(i -> {
            Stop[] stops = new Stop[]{new Stop(0, Color.BLACK), new Stop(0.2, axesColors[i]), new Stop(0.8, axesColors[i]), new Stop(1, Color.BLACK)};
            LinearGradient axisGradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
            gc.setStroke(axisGradient);
            gc.strokeLine(
                    transformForDrawing(points[i].getCoordinates()).x,
                    transformForDrawing(points[i].getCoordinates()).z,
                    transformForDrawing(points[points.length / 2 + i].getCoordinates()).x,
                    transformForDrawing(points[points.length / 2 + i].getCoordinates()).z
            );
        });
    }

    public static Vector3D transformForDrawing(Vector3D coordinate) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        return new Vector3D(new double[][]{
                {-1 * coordinate.x + bounds.getMaxX() / 2},
                {coordinate.y},
                {-1 * coordinate.z + bounds.getMaxY() / 2}
        });
    }

    @Override
    public String toString() {
        return "CoordinateSystem{" +
                "points=" + Arrays.toString(points) +
                '}';
    }

    public Point3D[] getPoints() {
        return points;
    }

}
