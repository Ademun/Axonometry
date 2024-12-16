package org.axonometry.geometry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.axonometry.geometry.CoordinateSystem.transformForDrawing;

public abstract class GeometricalPoint implements Transformable, Drawable {
    protected final String name;
    protected final Vector3D coordinates;
    protected final Location location;
    protected final double POINT_RADIUS = 2;

    public GeometricalPoint(String name, Vector3D coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        this.location = determineLocation();
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setFill(Color.WHITE);
        gc.fillOval(
                transformForDrawing(coordinates).x - POINT_RADIUS,
                transformForDrawing(coordinates).z - POINT_RADIUS, POINT_RADIUS * 2, POINT_RADIUS * 2
        );
        gc.fillText(
                name,
                transformForDrawing(coordinates).x - POINT_RADIUS * 2,
                transformForDrawing(coordinates).z - POINT_RADIUS * 4
        );
        gc.restore();
    }

    private Location determineLocation() {
        double x = coordinates.x;
        double y = coordinates.y;
        double z = coordinates.z;
        if (x == 0 && y == 0 && z == 0) {
            return Location.CENTER;
        } else if (x == 0 && y == 0) {
            return Location.Z_AXIS;
        } else if (x == 0 && z == 0) {
            return Location.Y_AXIS;
        } else if (y == 0 && z == 0) {
            return Location.X_AXIS;
        } else if (x == 0) {
            return Location.PROFILE_PLANE;
        } else if (y == 0) {
            return Location.FRONTAL_PLANE;
        } else if (z == 0) {
            return Location.HORIZONTAL_PLANE;
        }

        if (x > 0) {
            if (y > 0) {
                if (z > 0) {
                    return Location.OCTANT_1;
                } else {
                    return Location.OCTANT_4;
                }
            } else {
                if (z > 0) {
                    return Location.OCTANT_2;
                } else {
                    return Location.OCTANT_3;
                }
            }
        } else {
            if (y > 0) {
                if (z > 0) {
                    return Location.OCTANT_5;
                } else {
                    return Location.OCTANT_8;
                }
            } else {
                if (z > 0) {
                    return Location.OCTANT_6;
                } else {
                    return Location.OCTANT_7;
                }
            }
        }
    }
    public static boolean areCoplanar(GeometricalPoint[] points) {
        if (points.length <= 3) {
            return true;
        }
        Vector3D[] vectors = new Vector3D[3];
        IntStream.range(1, 4).forEach(i -> vectors[i - 1] = new Vector3D(points[0], points[i]));
        System.out.println(Arrays.toString(vectors));
        return Vector3D.areCoplanar(vectors);
    }
    public String toString() {
        return String.format("%s %s", name, coordinates.toString());
    }

    public String getName() {
        return name;
    }

    public Vector3D getCoordinates() {
        return coordinates;
    }

    public Location getLocation() {
        return location;
    }
}
