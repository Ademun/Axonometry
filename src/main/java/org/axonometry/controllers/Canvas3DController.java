package org.axonometry.controllers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.*;
import javafx.stage.Screen;
import org.axonometry.CanvasPane;
import org.axonometry.geometry.GeometricalObject;
import org.axonometry.geometry.Line3D;
import org.axonometry.geometry.Point3D;
import org.axonometry.models.Canvas3DModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Canvas3DController {
    private final Canvas3DModel model;
    private double mousePosX;
    private double mousePosY;
    private final HashSet<KeyCode> pressedKeys;
    private final double ZOOM_MULTIPLIER = 0.01;
    private final double ROTATION_MULTIPLIER = 200;
    @FXML
    CanvasPane canvasPane;

    public Canvas3DController(Canvas3DModel model) {
        this.model = model;
        this.pressedKeys = new HashSet<>();
    }

    public void initialize() {
        canvasPane.setCanvas(model.getCanvas());
        setListeners();
        setHandlers();
        model.transformCanvas();
    }

    private void setListeners() {
        model.xyRotationProperty().addListener(args -> model.transformCanvas());
        model.zRotationProperty().addListener(args -> model.transformCanvas());
        model.scaleProperty().addListener(args -> model.transformCanvas());
        model.objectCountProperty().addListener(args -> model.transformCanvas());
        model.getSelectedObjects().addListener((ListChangeListener<? super GeometricalObject>) observable -> {
            model.getCanvas().highlightObjects(new ArrayList<>(observable.getList()));
            model.transformCanvas();
        });
    }

    private void setHandlers() {
        model.getCanvas().sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            newValue.setOnScroll(event -> handleZoom(event));
            newValue.setOnMousePressed(event -> {
                if (event.getButton() != MouseButton.SECONDARY) {
                    return;
                }
                mousePosX = event.getSceneX();
                mousePosY = event.getSceneY();
            });
            newValue.setOnMouseDragged(event -> handleRotation(event));
            newValue.setOnMouseClicked(event -> handleSelection(event));
            newValue.setOnKeyPressed(event -> handleKeyPress(event));
            newValue.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
        });
    }

    private void handleZoom(ScrollEvent event) {
        double newScale = model.getScale() + event.getDeltaY() * ZOOM_MULTIPLIER;
        newScale = Math.clamp(newScale, 0.2, 50);
        model.setScale(newScale);
    }

    private void handleSelection(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY) {
            return;
        }
        canvasPane.requestFocus();
        boolean isShiftPressed = pressedKeys.contains(KeyCode.SHIFT);
        if (!isShiftPressed) {
            model.getSelectedObjects().clear();
        }
        GeometricalObject selectedObject = model.getCanvas().getSelectedObject(event.getX() - 250, event.getY() - 25, !isShiftPressed);
        if (selectedObject == null) {
            return;
        }
        model.getSelectedObjects().add(selectedObject);
    }

    private void handleKeyPress(KeyEvent event) {
        pressedKeys.add(event.getCode());
        pressedKeys.forEach(keyCode -> {
            switch (keyCode) {
                case DELETE -> deleteObjects();
                case L -> createLine();
            }
        });
    }

    private void handleRotation(MouseEvent event) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        if (event.getButton() != MouseButton.SECONDARY) {
            return;
        }
        double dx = (mousePosX - event.getSceneX());
        double dy = (mousePosY - event.getSceneY());
        if (event.isSecondaryButtonDown()) {
            model.setZRotation(model.getZRotation() - (ROTATION_MULTIPLIER * dx / bounds.getMaxY()));
            model.setXyRotation(model.getXyRotation() - (ROTATION_MULTIPLIER * dy / bounds.getMaxX()));
        }
        mousePosX = event.getSceneX();
        mousePosY = event.getSceneY();
    }

    private void deleteObjects() {
        model.getCanvas().removeObjects(new HashSet<>(model.getSelectedObjects()));
        model.setObjectCount(model.getObjectCount() - model.getSelectedObjects().size());
        model.getSelectedObjects().clear();
    }

    private void createLine() {
        List<Point3D> selectedPoints = model.getSelectedObjects().stream()
                .filter(object -> object instanceof Point3D)
                .map(point -> (Point3D) point).toList();
        Line3D line = new Line3D(selectedPoints.toArray(Point3D[]::new));
        model.getCanvas().addLine(line);
        model.setObjectCount(model.getObjectCount() + 1);
        model.getSelectedObjects().clear();
        model.getSelectedObjects().add(line);
    }
}
