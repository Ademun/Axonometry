package org.axonometry.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class CoordinateSystem implements GeometricalObject {
    private final Vertex3D[] vertices;

    public CoordinateSystem() {
        this.vertices = new Vertex3D[]{new Vertex3D("O", new Vector3D(new double[][]{{0}, {0}, {0}})),
                new Vertex3D("x", new Vector3D(new double[][]{{300}, {0}, {0}})),
                new Vertex3D("y", new Vector3D(new double[][]{{0}, {300}, {0}})),
                new Vertex3D("z", new Vector3D(new double[][]{{0}, {0}, {300}}))};
    }

    public CoordinateSystem(Vertex3D[] vertices) {
        this.vertices = vertices;
    }

    public CoordinateSystem transform(double dx, double dy, double dz, double rx, double ry, double rz, double scale) {
        Vertex3D[] transformed = new Vertex3D[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            transformed[i] = vertices[i].transform(dx, dy, dz, rx, ry, rz, scale);
        }
        return new CoordinateSystem(transformed);
    }

    public void draw(GraphicsContext gc) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        gc.setStroke(Color.GRAY);
        vertices[0].draw(gc);
        for (int i = 1; i < vertices.length; i++) {
            vertices[i].draw(gc);
            gc.strokeLine(
                    vertices[i].getCoordinates().getX() + bounds.getMaxX() / 2,
                    vertices[i].getCoordinates().getZ() + bounds.getMaxY() / 2,
                    -1 * vertices[i].getCoordinates().getX() + bounds.getMaxX() / 2,
                    -1 * vertices[i].getCoordinates().getZ() + bounds.getMaxY() / 2
            );
        }
    }

    public String toString() {
        return "XYZ";
    }
}
