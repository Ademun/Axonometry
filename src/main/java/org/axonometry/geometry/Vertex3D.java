package org.axonometry.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Vertex3D(String name, Coordinates coordinates) implements GeometricalObject, Serializable<Vertex3D> {

    public Vertex3D transform(double rx, double ry, double rz, double scale) {

        Coordinates transformed = new Coordinates(this
                .rotate(Matrix.getRotationalMatrix(rx, "x"))
                .rotate(Matrix.getRotationalMatrix(ry, "y"))
                .rotate(Matrix.getRotationalMatrix(rz, "z"))
                .scale(scale)
                .coordinates.getData());
        return new Vertex3D(name, transformed);
    }

    public Vertex3D translate(double dx, double dy, double dz) {
        Coordinates dCoordinates = new Coordinates(new double[][]{{dx}, {dy}, {dz}});
        return new Vertex3D(name, new Coordinates(coordinates.add(dCoordinates).getData()));
    }

    public void draw(GraphicsContext gc) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        gc.setFill(Color.WHITE);
        gc.fillOval(-1 * this.coordinates.getX() - 2 + bounds.getMaxX() / 2, -1 * this.coordinates.getZ() - 2 + bounds.getMaxY() / 2, 4, 4);
        gc.fillText(name, -1 * this.coordinates.getX() - 4 + bounds.getMaxX() / 2, -1 * this.coordinates.getZ() - 10 + bounds.getMaxY() / 2);
    }

    public Vertex3D rotate(Matrix rm) {
        Coordinates rotated = new Coordinates(rm.multi(this.coordinates).getData());
        return new Vertex3D(name, rotated);
    }

    public Vertex3D scale(double value) {
        Coordinates scaled = new Coordinates(this.coordinates.multi(value).getData());
        return new Vertex3D(name, scaled);
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return String.format("%s (%s, %s, %s)", name, df.format(coordinates.getX()), df.format(coordinates.getY()), df.format(coordinates.getZ()));
    }

    public Vertex3D fromString(String str) {
        Pattern pattern = Pattern.compile("[^)]*");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            double[][] coordinates = Arrays.stream(matcher.group(1).split(",")).map(coordinate -> new double[]{Double.parseDouble(coordinate)}).toArray(double[][]::new);
            return new Vertex3D(str.split(" ")[0], new Coordinates(coordinates));
        } else {
            throw new IllegalArgumentException("Invalid string");
        }

    }
}
