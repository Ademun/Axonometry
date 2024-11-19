package org.axonometry.geometry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
