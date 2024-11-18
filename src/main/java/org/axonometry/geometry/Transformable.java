package org.axonometry.geometry;

public interface Transformable {
    /**
     * Поворачивает фигуру на rx, ry, rz (В радианах) и увеличивает в scale раз
     */
    GeometricalObject transform(double rx, double ry, double rz, double scale);
}
