package org.axonometry.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.util.Arrays;
import java.util.stream.IntStream;

public class CoordinateSystem implements GeometricalObject {
    private final Vertex3D[] vertices;

    public CoordinateSystem() {
        this.vertices = new Vertex3D[]{
                new Vertex3D("x", new Vector3D(new double[][]{{300}, {0}, {0}})),
                new Vertex3D("y", new Vector3D(new double[][]{{0}, {300}, {0}})),
                new Vertex3D("z", new Vector3D(new double[][]{{0}, {0}, {300}})),
                new Vertex3D("-x", new Vector3D(new double[][]{{-300}, {0}, {0}})),
                new Vertex3D("-y", new Vector3D(new double[][]{{0}, {-300}, {0}})),
                new Vertex3D("-z", new Vector3D(new double[][]{{0}, {0}, {-300}}))};
    }

    public CoordinateSystem(Vertex3D[] vertices) {
        this.vertices = vertices;
    }

    public CoordinateSystem transform(double dx, double dy, double dz, double rx, double ry, double rz, double scale) {
        Vertex3D[] transformed = Arrays.stream(vertices)
                .map(vertex -> vertex.transform(dx, dy, dz, rx, ry, rz, scale))
                .toArray(Vertex3D[]::new);
        return new CoordinateSystem(transformed);
    }

    public void draw(GraphicsContext gc) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        gc.setStroke(Color.GRAY);
        IntStream.range(0, vertices.length / 2).forEach(i -> {
            vertices[i].draw(gc);
            gc.strokeLine(
                    -1 * vertices[i].getCoordinates().getX() + bounds.getMaxX() / 2,
                    -1 * vertices[i].getCoordinates().getZ() + bounds.getMaxY() / 2,
                    -1 * vertices[vertices.length / 2 + i].getCoordinates().getX() + bounds.getMaxX() / 2,
                    -1 * vertices[vertices.length / 2 + i].getCoordinates().getZ() + bounds.getMaxY() / 2
            );
        });
    }

    public String toString() {
        return "XYZ";
    }

    public Vertex3D[] getVertices() {
        return vertices;
    }
}
