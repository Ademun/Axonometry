package org.axonometry.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vertex3D implements GeometricalObject, Clickable {
    private String name;
    private final Vector3D coordinates;
    private final double radius;
    private boolean isSelected;

    public Vertex3D(String name, Vector3D coordinates, double radius) {
        this.name = name;
        this.coordinates = coordinates;
        this.radius = radius;
    }
    public Vertex3D(String name, Vector3D coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        this.radius = 2;
    }

    public Vertex3D transform(double dx, double dy, double dz, double rx, double ry, double rz, double scale) {
        Vector3D dVector3D = new Vector3D(new double[][]{{dx}, {dy}, {dz}});
        Vertex3D translated = new Vertex3D(name, new Vector3D(coordinates.add(dVector3D).getData()));
        Vector3D transformed = new Vector3D(translated
                .rotate(Matrix.getRotationalMatrix(new Vector3D(new double[][]{{1}, {0}, {0}}), rx))
                .rotate(Matrix.getRotationalMatrix(new Vector3D(new double[][]{{0}, {1}, {0}}), ry))
                .rotate(Matrix.getRotationalMatrix(new Vector3D(new double[][]{{0}, {0}, {1}}), rz))
                .scale(scale)
                .coordinates.getData());
        return new Vertex3D(name, transformed);
    }
    public Vertex3D rotate(Matrix rm) {
        Vector3D rotated = new Vector3D(rm.multi(this.coordinates).getData());
        return new Vertex3D(name, rotated);
    }

    public Vertex3D scale(double value) {
        Vector3D scaled = new Vector3D(this.coordinates.multi(value).getData());
        return new Vertex3D(name, scaled);
    }

    public void draw(GraphicsContext gc) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        if (isSelected) {
            gc.setFill(Color.rgb(255, 140, 0, 0.5));
            gc.fillOval(
                    -1 * this.coordinates.getX() - radius * 2 + + bounds.getMaxX() / 2,
                    -1 * this.coordinates.getZ() - radius * 2 + bounds.getMaxY() / 2, radius * 4, radius * 4
            );
            gc.setStroke(Color.ORANGE);
            gc.strokeOval(
                    -1 * this.coordinates.getX() - radius * 2 + bounds.getMaxX() / 2,
                    -1 * this.coordinates.getZ() - radius * 2 + bounds.getMaxY() / 2, radius * 4, radius * 4
            );
        }
        gc.setFill(Color.WHITE);
        gc.fillOval(
                -1 * this.coordinates.getX() - radius + bounds.getMaxX() / 2,
                -1 * this.coordinates.getZ() - radius + bounds.getMaxY() / 2, radius * 2, radius * 2
        );
        gc.fillText(
                name,
                -1 * this.coordinates.getX() - radius * 2 + bounds.getMaxX() / 2,
                -1 * this.coordinates.getZ() - radius * 4 + bounds.getMaxY() / 2
        );
    }

    public Vertex3D projectOn(Plane plane, String name) {
        Vector3D normal = plane.getNormal();
        Vertex3D planeVertex = plane.getVertices()[0];
        double param = -1 * (
                normal.getX() * coordinates.getX()
                + normal.getY() * coordinates.getY()
                + normal.getZ() * coordinates.getZ()
                + -1 * (
                        normal.getX() * planeVertex.getCoordinates().getX()
                        + normal.getY() * planeVertex.getCoordinates().getY()
                        + normal.getZ() * planeVertex.getCoordinates().getZ()
                )
        ) / (normal.getX() * normal.getX() + normal.getY() * normal.getY() + normal.getZ() * normal.getZ());
        return new Vertex3D(name, new Vector3D(new double[][]{
                {normal.getX() * param + coordinates.getX()},
                {normal.getY() * param + coordinates.getY()},
                {normal.getZ() * param + coordinates.getZ()}
        }), 2);
    }
    public boolean isClicked(double x, double y) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        return Math.pow(x - (-1 * this.coordinates.getX() - radius + bounds.getMaxX() / 2), 2) + Math.pow(y - (-1 * this.coordinates.getZ() - radius + bounds.getMaxY() / 2), 2) <= 48;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return String.format("%s (%s, %s, %s)", name, df.format(coordinates.getX()), df.format(coordinates.getY()), df.format(coordinates.getZ()));
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
