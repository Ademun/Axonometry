package org.axonometry.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.axonometry.Canvas3D;
import org.axonometry.geometry.GeometricalObject;

public class Canvas3DModel {
    private final Canvas3D canvas;
    private final IntegerProperty objectCount;
    private final DoubleProperty scale;
    private final DoubleProperty xyRotation;
    private final DoubleProperty zRotation;
    private final ObservableList<GeometricalObject> selectedObjects;

    public Canvas3DModel() {
        this.canvas = new Canvas3D();
        this.objectCount = new SimpleIntegerProperty(canvas.getObjects().size() - 1);
        this.scale = new SimpleDoubleProperty(1);
        this.xyRotation = new SimpleDoubleProperty(10);
        this.zRotation = new SimpleDoubleProperty(45);
        this.selectedObjects = FXCollections.observableArrayList();
    }

    public int getObjectCount() {
        return objectCount.get();
    }

    public IntegerProperty objectCountProperty() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount.set(objectCount);
    }

    public double getScale() {
        return scale.get();
    }

    public DoubleProperty scaleProperty() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale.set(scale);
    }

    public double getXyRotation() {
        return xyRotation.get();
    }

    public DoubleProperty xyRotationProperty() {
        return xyRotation;
    }

    public void setXyRotation(double xyRotation) {
        this.xyRotation.set(xyRotation);
    }

    public double getZRotation() {
        return zRotation.get();
    }

    public DoubleProperty zRotationProperty() {
        return zRotation;
    }

    public void setZRotation(double zRotation) {
        this.zRotation.set(zRotation);
    }

    public ObservableList<GeometricalObject> getSelectedObjects() {
        return selectedObjects;
    }

    public Canvas3D getCanvas() {
        return canvas;
    }
}
