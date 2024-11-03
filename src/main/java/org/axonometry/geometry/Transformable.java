package org.axonometry.geometry;

public interface Transformable {
    //Смещает фигуру по осям x, y, z на dx, dy, dz и поворачивает её на rx, ry, rz (В радианах)
    GeometricalObject transform(double dx, double dy, double dz, double rx, double ry, double rz, double scale);
}
