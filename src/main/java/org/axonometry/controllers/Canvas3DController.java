package org.axonometry.controllers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Screen;
import org.axonometry.CanvasPane;
import org.axonometry.geometry.GeometricalObject;
import org.axonometry.geometry.Vertex3D;
import org.axonometry.models.Canvas3DModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Canvas3DController {
    private final Canvas3DModel model;
    private double mousePosX;
    private double mousePosY;
    private final HashSet<KeyCode> pressedKeys;
    private final double MOUSE_SCROLL_MULTIPLIER = 0.01;
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
        transformCanvas();
    }

    private void setListeners() {
        model.xyRotationProperty().addListener(args -> transformCanvas());
        model.zRotationProperty().addListener(args -> transformCanvas());
        model.scaleProperty().addListener(args -> transformCanvas());
        model.objectCountProperty().addListener(args -> transformCanvas());
        model.getSelectedObjects().addListener((ListChangeListener<? super GeometricalObject>) observable -> {
            var selectedObjectsList =  observable.getList();
            model.getCanvas().highlightObjects(new ArrayList<>(selectedObjectsList));
        });
    }

    private void setHandlers() {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        model.getCanvas().sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            newValue.setOnScroll(event -> {
                double newScale = model.getScale() + event.getDeltaY() * MOUSE_SCROLL_MULTIPLIER;
                newScale = Math.clamp(newScale, 0.2, 50);
                model.setScale(newScale);
            });
            newValue.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    mousePosX = event.getSceneX();
                    mousePosY = event.getSceneY();
                }
            });
            newValue.setOnMouseDragged(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    double dx = (mousePosX - event.getSceneX());
                    double dy = (mousePosY - event.getSceneY());
                    if (event.isSecondaryButtonDown()) {
                        model.setZRotation(model.getZRotation() - (200 * dx / bounds.getMaxY()));
                        model.setXyRotation(model.getXyRotation() - (200 * dy / bounds.getMaxX()));
                    }
                    mousePosX = event.getSceneX();
                    mousePosY = event.getSceneY();
                }
            });
            newValue.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    boolean isShiftPressed = pressedKeys.contains(KeyCode.SHIFT);
                    canvasPane.requestFocus();
                    GeometricalObject selectedObject = model.getCanvas().getClickedObject(event.getX() - 250, event.getY() - 25, !isShiftPressed);
                    if (!isShiftPressed) {
                        model.getSelectedObjects().clear();
                    }
                    if (selectedObject == null) {
                        return;
                    }
                    model.getSelectedObjects().add(selectedObject);
                }
            });
            newValue.setOnKeyPressed(event -> {
                pressedKeys.add(event.getCode());
                handleKeyPress();
            });
            newValue.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
        });
    }

    private void handleKeyPress() {
        pressedKeys.forEach(keyCode -> {
            switch (keyCode) {
                case DELETE -> deleteObjects();
                case P -> createPlane();
            }
        });
    }

    private void deleteObjects() {
        model.getCanvas().removeObjects(new HashSet<>(model.getSelectedObjects()));
        model.setObjectCount(model.getObjectCount() - model.getSelectedObjects().size());
        model.getSelectedObjects().clear();
    }
    private void createPlane() {
//        if (model.getSelectedObjects().size() != 3) return;
        List<Vertex3D> selectedVertices = model.getSelectedObjects().stream()
                .filter(object -> object instanceof Vertex3D)
                .map(vertex -> (Vertex3D) vertex).toList();
        model.getCanvas().addPlane(new ArrayList<>(selectedVertices));
        model.setObjectCount(model.getObjectCount() + 1);
    }

    private void transformCanvas() {
        model.getCanvas().transform(
                -1 * model.getXyRotation() * Math.PI / 180,
                model.getXyRotation() * Math.PI / 180,
                model.getZRotation() * Math.PI / 180,
                model.getScale()
        );
    }
}
