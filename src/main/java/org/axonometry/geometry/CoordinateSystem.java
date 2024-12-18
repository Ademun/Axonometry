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
    private final Vertex3D[] vertices;

    public CoordinateSystem() {
        this.vertices = new Vertex3D[]{
                new Vertex3D("x", new Vector3D(new double[][]{{1000}, {0}, {0}})),
                new Vertex3D("y", new Vector3D(new double[][]{{0}, {1000}, {0}})),
                new Vertex3D("z", new Vector3D(new double[][]{{0}, {0}, {1000}})),
                new Vertex3D("-x", new Vector3D(new double[][]{{-1000}, {0}, {0}})),
                new Vertex3D("-y", new Vector3D(new double[][]{{0}, {-1000}, {0}})),
                new Vertex3D("-z", new Vector3D(new double[][]{{0}, {0}, {-1000}}))};
    }

    public CoordinateSystem(Vertex3D[] vertices) {
        this.vertices = vertices;
    }

    public CoordinateSystem transform(double rx, double ry, double rz, double scale) {
        Vertex3D[] transformed = Arrays.stream(vertices)
                .map(vertex -> vertex.transform(rx, ry, rz, scale))
                .toArray(Vertex3D[]::new);
        return new CoordinateSystem(transformed);
    }

    public void draw(GraphicsContext gc) {
        Color[] axesColors = new Color[]{Color.RED.desaturate(), Color.BLUE.desaturate(), Color.GREEN.desaturate()};
        IntStream.range(0, vertices.length / 2).forEach(i -> {
            Stop[] stops = new Stop[]{new Stop(0, Color.BLACK), new Stop(0.2, axesColors[i]), new Stop(0.8, axesColors[i]), new Stop(1, Color.BLACK)};
            LinearGradient axisGradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
            gc.setStroke(axisGradient);
            gc.strokeLine(
                    transformForDrawing(vertices[i].getCoordinates()).x,
                    transformForDrawing(vertices[i].getCoordinates()).z,
                    transformForDrawing(vertices[vertices.length / 2 + i].getCoordinates()).x,
                    transformForDrawing(vertices[vertices.length / 2 + i].getCoordinates()).z
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
                "vertices=" + Arrays.toString(vertices) +
                '}';
    }

    public Vertex3D[] getVertices() {
        return vertices;
    }

}
