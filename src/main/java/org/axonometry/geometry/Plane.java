package org.axonometry.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Plane implements GeometricalObject {
    private final Vertex3D[] vertices;
    private final Color color;

    public Plane(Vertex3D[] vertices) {
        Vector3D[] vectors = new Vector3D[vertices.length - 1];
        for (int i = 1; i < vertices.length; i++) {
            vectors[i - 1] = new Vector3D(vertices[0], vertices[i]);
        }
        if (!Vector3D.areCoplanar(vectors)) throw new IllegalArgumentException("Vertices aren't coplanar");
        Random random = new Random();
        this.vertices = vertices;
        this.color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0.25);
    }

    public Plane(Vertex3D[] vertices, Color color) {
        this.vertices = vertices;
        this.color = color;
    }

    public Plane transform(double dx, double dy, double dz, double rx, double ry, double rz, double scale) {
        Vertex3D[] transformed = new Vertex3D[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            transformed[i] = vertices[i].transform(dx, dy, dz, rx, ry, rz, scale);
        }
        return new Plane(transformed, color);
    }

    public void draw(GraphicsContext gc) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double[] xPoints = new double[vertices.length];
        double[] yPoints = new double[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].draw(gc);
            xPoints[i] = -1 * vertices[i].getCoordinates().getX() + bounds.getMaxX() / 2;
            yPoints[i] = -1 * vertices[i].getCoordinates().getZ() + bounds.getMaxY() / 2;
        }
        gc.setFill(color);
        gc.setStroke(Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0.9));
        gc.setLineDashes(10);
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
}
