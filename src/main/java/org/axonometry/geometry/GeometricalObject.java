package org.axonometry.geometry;

public interface GeometricalObject extends Transformable, Drawable {
    Vertex3D[] getVertices();
}
