package org.axonometry.geometry;

public interface GeometricalObject extends Transformable, Drawable {
    public Vertex3D[] getVertices();
}
