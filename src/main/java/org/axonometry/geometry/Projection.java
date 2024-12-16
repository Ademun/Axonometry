package org.axonometry.geometry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static org.axonometry.geometry.CoordinateSystem.transformForDrawing;
import static org.axonometry.geometry.Matrix.getRotationalMatrix;

public class Projection extends GeometricalPoint implements GeometricalObject {
    public Projection(String name, Vector3D coordinates) {
        super(name, coordinates);
    }

    @Override
    public Projection transform(double rx, double ry, double rz, double scale) {
        Vector3D transformedCoordinates = new Vector3D(this
                .rotate(getRotationalMatrix(new Vector3D(new double[][]{{1}, {0}, {0}}), rx))
                .rotate(getRotationalMatrix(new Vector3D(new double[][]{{0}, {1}, {0}}), ry))
                .rotate(getRotationalMatrix(new Vector3D(new double[][]{{0}, {0}, {1}}), rz))
                .scale(scale).getCoordinates().getData());
        return new Projection(name, transformedCoordinates);
    }

    private Projection rotate(Matrix rm) {
        Vector3D rotated = new Vector3D(rm.multi(coordinates).getData());
        return new Projection(name, rotated);
    }

    private Projection scale(double value) {
        Vector3D scaled = new Vector3D(coordinates.multi(value).getData());
        return new Projection(name, scaled);
    }
    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        Font oldFont = gc.getFont();
        gc.setFont(new Font(oldFont.getName(), oldFont.getSize() * 0.95));
        gc.setFill(Color.WHITE.desaturate().darker());
        gc.fillOval(
                transformForDrawing(coordinates).x - POINT_RADIUS / 2,
                transformForDrawing(coordinates).z - POINT_RADIUS / 2, POINT_RADIUS, POINT_RADIUS
        );
        gc.fillText(
                name,
                transformForDrawing(coordinates).x - POINT_RADIUS,
                transformForDrawing(coordinates).z - POINT_RADIUS * 2
        );
        gc.restore();
    }

    @Override
    public Projection[] getPoints() {
        return new Projection[]{this};
    }
}
