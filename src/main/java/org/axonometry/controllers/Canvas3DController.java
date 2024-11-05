package org.axonometry.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import org.axonometry.CanvasPane;
import org.axonometry.geometry.GeometricalObject;
import org.axonometry.geometry.Vertex3D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Canvas3DController {
    @FXML
    public CanvasPane canvasPane;
    public TextField vertexField;
    public Label objectCount;
    public Label rotation;
    private HashSet<GeometricalObject> selectedObjects;
    private HashSet<KeyCode> pressedKeys;
    private double scale;
    private double mousePosX;
    private double mousePosY;
    private double xyRotation;
    private double zRotation;

    public void initialize() {
        scale = 1;
        xyRotation = 10;
        zRotation = 45;
        transformCanvas();
        setListeners();
        selectedObjects = new HashSet<>();
        pressedKeys = new HashSet<>();
        setObjectsCount();
    }

    private void setListeners() {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        canvasPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                canvasPane.getScene().setOnMousePressed(event -> {
                    mousePosX = event.getSceneX();
                    mousePosY = event.getSceneY();
                });
                canvasPane.getScene().setOnMouseDragged(event -> {
                    double dx = (mousePosX - event.getSceneX());
                    double dy = (mousePosY - event.getSceneY());
                    if (event.isPrimaryButtonDown()) {
                        zRotation -= (200 * dx / bounds.getMaxY());
                        xyRotation += (200 * dy / bounds.getMaxX());
                        transformCanvas();
                    }
                    mousePosX = event.getSceneX();
                    mousePosY = event.getSceneY();
                });
                canvasPane.getScene().setOnScroll(event -> {
                    scale += event.getDeltaY() * 0.025;
                    scale = Math.max(0.5, Math.min(50, scale));
                    transformCanvas();
                });
                canvasPane.getScene().setOnMouseClicked(event -> {
                    boolean containsShift = pressedKeys.contains(KeyCode.SHIFT);
                    canvasPane.requestFocus();
                    GeometricalObject selectedObject = canvasPane
                            .getCanvas()
                            .getClickedObject(event.getX() - 175.2, event.getY() - 25.6, !containsShift);
                    if (selectedObject != null) {
                        if (containsShift) {
                            selectedObjects.add(selectedObject);
                        } else {
                            selectedObjects.clear();
                            selectedObjects.add(selectedObject);
                        }
                    }
                });
                canvasPane.getScene().setOnKeyPressed(event -> {
                    pressedKeys.add(event.getCode());
                    handleKeyPress();
                });
                canvasPane.getScene().setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
            }
        });
        vertexField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) createVertex();
        });
    }

    private void handleKeyPress() {
        pressedKeys.forEach(keyCode -> {
            switch (keyCode) {
                case DELETE -> deleteObjects();
                case ENTER -> createPlane();
            }
        });
    }

    private void createVertex() {
        canvasPane.getCanvas().addVertex(Vertex3D.fromString(vertexField.getText()));
        transformCanvas();
        setObjectsCount();
    }

    private void createPlane() {
        if (selectedObjects.size() != 3) return;
        List<Vertex3D> selectedVertices = selectedObjects.stream()
                .filter(object -> object instanceof Vertex3D)
                .map(vertex -> (Vertex3D) vertex).toList();
        canvasPane.getCanvas().addPlane(new ArrayList<>(selectedVertices));
        transformCanvas();
        setObjectsCount();
    }

    private void transformCanvas() {
        rotation.setText("X: " + Math.round(xyRotation) + " Y: " + Math.round(xyRotation) + " Z: " + Math.round(zRotation));
        canvasPane.getCanvas().transform(
                -1 * xyRotation * Math.PI / 180,
                xyRotation * Math.PI / 180,
                zRotation * Math.PI / 180,
                scale
        );
    }

    private void deleteObjects() {
        canvasPane.getCanvas().removeObjects(selectedObjects);
        setObjectsCount();
    }


    private void setObjectsCount() {
        objectCount.setText("Объекты: " + (canvasPane.getCanvas().getObjects().size() - 1));
    }
}

