package org.axonometry.geometry;

public interface Transformable {
    GeometricalObject transform(double dx, double dy, double dz, double rx, double ry, double rz, double scale);
}
