package org.axonometry.geometry;

public interface Transformable {
    GeometricalObject transform(double rx, double ry, double rz, double scale);
}
