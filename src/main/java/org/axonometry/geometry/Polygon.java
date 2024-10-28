package org.axonometry.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Polygon implements GeometricalObject, Destructible<Vertex3D> {
    private final Vertex3D[] vertices;
    private final Color color;

    public Polygon(Vertex3D[] vertices) {
        Random random = new Random();
        this.vertices = vertices;
        this.color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0.25);
    }

    public Polygon(Vertex3D[] vertices, Color color) {
        this.vertices = vertices;
        this.color = color;
    }

    public Polygon transform(double rx, double ry, double rz, double scale) {
        Vertex3D[] transformed = new Vertex3D[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            transformed[i] = vertices[i].transform(rx, ry, rz, scale);
        }
        return new Polygon(transformed, color);
    }

    public Polygon translate(double dx, double dy, double dz) {
        Vertex3D[] translated = new Vertex3D[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            translated[i] = vertices[i].translate(dx, dy, dz);
        }
        return new Polygon(translated, color);
    }

    public void draw(GraphicsContext gc) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double[] xPoints = new double[vertices.length];
        double[] yPoints = new double[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].draw(gc);
            xPoints[i] = -1 * vertices[i].coordinates().getX() + bounds.getMaxX() / 2;
            yPoints[i] = -1 * vertices[i].coordinates().getZ() + bounds.getMaxY() / 2;
            for (int j = 0; j < i; j++) {
                gc.setStroke(Color.WHITE);
                gc.strokeLine(
                        -1 * vertices[i].coordinates().getX() + bounds.getMaxX() / 2,
                        -1 * vertices[i].coordinates().getZ() + bounds.getMaxY() / 2,
                        -1 * vertices[j].coordinates().getX() + bounds.getMaxX() / 2,
                        -1 * vertices[j].coordinates().getZ() + bounds.getMaxY() / 2
                );
            }
        }
        gc.setFill(color);
        gc.fillPolygon(xPoints, yPoints, vertices.length);
    }

    @Override
    public Vertex3D[] destruct() {
        return vertices;
    }

    public String toString() {
        return String.format("Polygon: %s\n%s",
                Arrays.stream(vertices).map(Vertex3D::name).collect(Collectors.joining("")),
                Arrays.stream(vertices).map(Vertex3D::toString).collect(Collectors.joining("\n"))
        );
    }

    public Polygon fromString(String str) {
        String[] strings = str.split("\n");
        Vertex3D[] vertices = new Vertex3D[strings.length - 1];
        for (int i = 1; i < strings.length; i++) {
            //vertices[] = string[i]
        }
        return null;
    }
}
