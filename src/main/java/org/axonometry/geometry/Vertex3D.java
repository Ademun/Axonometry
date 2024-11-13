package org.axonometry.geometry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.axonometry.Location;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.axonometry.geometry.CoordinateSystem.transformForDrawing;
public class Vertex3D implements GeometricalObject, Clickable {
    private String name;
    private final Vector3D coordinates;
    private final double radius;
    private final Location location;
    private boolean isSelected;

    public Vertex3D(String name, Vector3D coordinates, double radius) {
        this.name = name;
        this.coordinates = coordinates;
        this.radius = radius;
        this.location = Location.OCTANT_1;
    }
    public Vertex3D(String name, Vector3D coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        this.radius = 2;
        this.location = Location.OCTANT_1;
    }

    public Vertex3D transform(double rx, double ry, double rz, double scale) {
        Vector3D transformed = new Vector3D(this
                .rotate(Matrix.getRotationalMatrix(new Vector3D(new double[][]{{1}, {0}, {0}}), rx))
                .rotate(Matrix.getRotationalMatrix(new Vector3D(new double[][]{{0}, {1}, {0}}), ry))
                .rotate(Matrix.getRotationalMatrix(new Vector3D(new double[][]{{0}, {0}, {1}}), rz))
                .scale(scale)
                .coordinates.getData());
        return new Vertex3D(name, transformed);
    }
    public Vertex3D rotate(Matrix rm) {
        Vector3D rotated = new Vector3D(rm.multi(coordinates).getData());
        return new Vertex3D(name, rotated);
    }

    public Vertex3D scale(double value) {
        Vector3D scaled = new Vector3D(coordinates.multi(value).getData());
        return new Vertex3D(name, scaled);
    }

    public void draw(GraphicsContext gc) {
        if (isSelected) {
            gc.setFill(Color.rgb(255, 140, 0, 0.5));
            gc.fillOval(
                    transformForDrawing(coordinates).x - radius * 2,
                    transformForDrawing(coordinates).z - radius * 2, radius * 4, radius * 4
            );
            gc.setStroke(Color.ORANGE);
            gc.strokeOval(
                    transformForDrawing(coordinates).x - radius * 2,
                    transformForDrawing(coordinates).z - radius * 2, radius * 4, radius * 4
            );
        }
        gc.setFill(Color.WHITE);
        gc.fillOval(
                transformForDrawing(coordinates).x - radius,
                transformForDrawing(coordinates).z - radius, radius * 2, radius * 2
        );
        gc.fillText(
                name,
                transformForDrawing(coordinates).x - radius * 2,
                transformForDrawing(coordinates).z - radius * 4
        );
    }
    public boolean isClicked(double x, double y) {
        int CLICK_AREA = 48;
        return Math.pow(x - transformForDrawing(this.coordinates).x - radius, 2) + Math.pow(y - transformForDrawing(this.coordinates).z - radius / 2, 2) <= CLICK_AREA;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return String.format("%s (%s, %s, %s)", name, df.format(coordinates.x), df.format(coordinates.y), df.format(coordinates.z));
    }

    public static Vertex3D fromString(String str) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            double[][] coordinates = Arrays.stream(matcher.group(1).split(",")).map(coordinate -> new double[]{Double.parseDouble(coordinate)}).toArray(double[][]::new);
            return new Vertex3D(str.split("\\(")[0], new Vector3D(coordinates));
        } else throw new IllegalArgumentException("Invalid string");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector3D getCoordinates() {
        return coordinates;
    }

    public Vertex3D[] getVertices() {
        return new Vertex3D[]{this};
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }
}
