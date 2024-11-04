package org.axonometry.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Plane implements GeometricalObject {
    private final Vertex3D[] vertices;
    private final Vector3D normal;
    private final Color color;
    private boolean isSelected;

    public Plane(Vertex3D[] vertices) {
        Vector3D[] vectors = new Vector3D[vertices.length - 1];
        IntStream.range(1, vertices.length)
                .forEach(i -> vectors[i - 1] = new Vector3D(vertices[0], vertices[i]));
        if (!Vector3D.areCoplanar(vectors)) throw new IllegalArgumentException("Vertices aren't coplanar");
        Random random = new Random();
        this.vertices = vertices;
        this.normal = Vector3D.vectorProduct(vectors[0], vectors[1]).ort();
        this.color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0.25);
    }

    public Plane(Vertex3D[] vertices, Vector3D normal, Color color) {
        this.vertices = vertices;
        this.normal = normal;
        this.color = color;
    }

    public Plane transform(double dx, double dy, double dz, double rx, double ry, double rz, double scale) {
        Vertex3D[] transformed = Arrays.stream(vertices)
                .map(vertex -> vertex.transform(dx, dy, dz, rx, ry, rz, scale))
                .toArray(Vertex3D[]::new);
        return new Plane(transformed, normal, color);
    }

    public void draw(GraphicsContext gc) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double[] xPoints = new double[vertices.length];
        double[] yPoints = new double[vertices.length];
        IntStream.range(0, vertices.length).forEach(i -> {
            vertices[i].draw(gc);
            xPoints[i] = -1 * vertices[i].getCoordinates().getX() + bounds.getMaxX() / 2;
            yPoints[i] = -1 * vertices[i].getCoordinates().getZ() + bounds.getMaxY() / 2;
        });
        gc.setLineDashes(10);
        gc.setStroke(Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0.9));
        gc.setFill(color);
        if (isSelected) {
            gc.setStroke(Color.ORANGE);
            gc.setFill(Color.rgb(255, 140, 0, 0.9));
            gc.setLineDashes(0);
        }
        gc.strokePolygon(xPoints, yPoints, vertices.length);
        gc.setLineDashes(0);
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
