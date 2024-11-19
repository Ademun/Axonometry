package org.axonometry.geometry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.axonometry.geometry.CoordinateSystem.transformForDrawing;
import static org.axonometry.geometry.Matrix.getRotationalMatrix;

public class Point3D extends GeometricalPoint implements GeometricalObject, Selectable {
    private final Projection[] projections;
    private boolean enableProjections = true;
    private boolean isSelected = false;
    private final int CLICK_AREA = 48;

    public Point3D(String name, Vector3D coordinates) {
        super(name, coordinates);
        this.projections = generateProjections();
    }

    public Point3D(String name, Vector3D coordinates, Projection[] projections, boolean isSelected, boolean enableProjections) {
        super(name, coordinates);
        this.projections = projections;
        this.isSelected = isSelected;
        this.enableProjections = enableProjections;
    }

    @Override
    public Point3D transform(double rx, double ry, double rz, double scale) {
        Projection[] transformedProjections = Arrays.stream(projections)
                .map(projection -> projection.transform(rx, ry, rz, scale))
                .toArray(Projection[]::new);
        Vector3D transformedCoordinates = new Vector3D(this
                .rotate(getRotationalMatrix(new Vector3D(new double[][]{{1}, {0}, {0}}), rx))
                .rotate(getRotationalMatrix(new Vector3D(new double[][]{{0}, {1}, {0}}), ry))
                .rotate(getRotationalMatrix(new Vector3D(new double[][]{{0}, {0}, {1}}), rz))
                .scale(scale).getCoordinates().getData());
        return new Point3D(name, transformedCoordinates, transformedProjections, isSelected, enableProjections);
    }

    private Point3D rotate(Matrix rm) {
        Vector3D rotated = new Vector3D(rm.multi(coordinates).getData());
        return new Point3D(name, rotated);
    }

    private Point3D scale(double value) {
        Vector3D scaled = new Vector3D(coordinates.multi(value).getData());
        return new Point3D(name, scaled);
    }

    public void draw(GraphicsContext gc) {
        super.draw(gc);
        if (enableProjections) {
            drawProjections(gc);
        }
        if (isSelected) {
            drawSelectionCircle(gc);
        }
    }

    private void drawProjections(GraphicsContext gc) {
        gc.setLineDashes(20);
        gc.setStroke(Color.rgb(255, 255, 255, 0.5));
        Arrays.stream(projections).forEach(projection -> {
            gc.strokeLine(
                    transformForDrawing(projection.getCoordinates()).x,
                    transformForDrawing(projection.getCoordinates()).z,
                    transformForDrawing(coordinates).x,
                    transformForDrawing(coordinates).z
            );
            projection.draw(gc);
        });
        gc.setLineDashes(0);
        gc.setStroke(Color.WHITE);
    }

    private void drawSelectionCircle(GraphicsContext gc) {
        gc.setFill(Color.rgb(255, 140, 0, 0.5));
        gc.fillOval(
                transformForDrawing(coordinates).x - POINT_RADIUS * 2,
                transformForDrawing(coordinates).z - POINT_RADIUS * 2, POINT_RADIUS * 4, POINT_RADIUS * 4
        );
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.ORANGE);
        gc.strokeOval(
                transformForDrawing(coordinates).x - POINT_RADIUS * 2,
                transformForDrawing(coordinates).z - POINT_RADIUS * 2, POINT_RADIUS * 4, POINT_RADIUS * 4
        );
        gc.setStroke(Color.WHITE);
    }

    @Override
    public boolean isClicked(double x, double y) {
        return Math.pow(x - transformForDrawing(this.coordinates).x - POINT_RADIUS, 2) + Math.pow(y - transformForDrawing(this.coordinates).z - POINT_RADIUS / 2, 2) <= CLICK_AREA;
    }

    private Projection[] generateProjections() {
        Projection[] projections = new Projection[3];
        //Проекция на XY
        projections[0] = new Projection(name + "'",
                new Vector3D(new double[][]{{coordinates.x}, {coordinates.y}, {0}})
        );
        //Проекция на XZ
        projections[1] = new Projection(name + "''",
                new Vector3D(new double[][]{{coordinates.x}, {0}, {coordinates.z}})
        );
        //Проекция на YZ
        projections[2] = new Projection(name + "'''",
                new Vector3D(new double[][]{{0}, {coordinates.y}, {coordinates.z}})
        );
        return projections;
    }

    public static Point3D fromString(String str) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid string");
        }
        double[][] coordinates = Arrays.stream(matcher.group(1).split(","))
                .map(coordinate -> new double[]{Double.parseDouble(coordinate)})
                .toArray(double[][]::new);
        return new Point3D(str.split("\\(")[0], new Vector3D(coordinates));
    }

    public Projection[] getProjections() {
        return projections;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isEnableProjections() {
        return enableProjections;
    }

    public void setEnableProjections(boolean enableProjections) {
        this.enableProjections = enableProjections;
    }

    @Override
    public Point3D[] getPoints() {
        return new Point3D[]{this};
    }
}
