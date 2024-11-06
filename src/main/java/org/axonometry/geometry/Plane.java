package org.axonometry.geometry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.axonometry.geometry.CoordinateSystem.transformForDrawing;

public class Plane implements GeometricalObject {
    private final Vertex3D[] vertices;
    private final Vector3D normal;
    private final Color color;

    public Plane(Vertex3D[] vertices, Color color) {
        if (vertices.length != 3) throw new IllegalArgumentException("Plane should consist of only 3 vertices");
        Vector3D[] vectors = new Vector3D[vertices.length - 1];
        IntStream.range(1, vertices.length)
                .forEach(i -> vectors[i - 1] = new Vector3D(vertices[0], vertices[i]));
        this.vertices = vertices;
        this.normal = Vector3D.vectorProduct(vectors[0], vectors[1]).ort();
        this.color = color;
    }

    public Plane transform(double rx, double ry, double rz, double scale) {
        Vertex3D[] transformed = Arrays.stream(vertices)
                .map(vertex -> vertex.transform(rx, ry, rz, scale))
                .toArray(Vertex3D[]::new);
        return new Plane(transformed, color);
    }

    public void draw(GraphicsContext gc) {
        double[] xPoints = new double[vertices.length];
        double[] yPoints = new double[vertices.length];
        IntStream.range(0, vertices.length).forEach(i -> {
            vertices[i].draw(gc);
            xPoints[i] = transformForDrawing(vertices[i].getCoordinates()).getX();
            yPoints[i] = transformForDrawing(vertices[i].getCoordinates()).getZ();
        });
        gc.setLineDashes(10);
        gc.setStroke(Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0.9));
        gc.strokePolygon(xPoints, yPoints, vertices.length);
        gc.setLineDashes(0);
        gc.setFill(color);
        gc.fillPolygon(xPoints, yPoints, vertices.length);
    }

    public String toString() {
        return String.format("Plane: %s\n%s",
                Arrays.stream(vertices).map(Vertex3D::getName).collect(Collectors.joining("")),
                Arrays.stream(vertices).map(Vertex3D::toString).collect(Collectors.joining("\n"))
        );
    }

    public Vertex3D[] getVertices() {
        return vertices;
    }

    public Vector3D getNormal() {
        return normal;
    }
}
