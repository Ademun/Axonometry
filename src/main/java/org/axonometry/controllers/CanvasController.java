package org.axonometry.controllers;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.axonometry.CanvasPane;
import org.axonometry.geometry.GeometricalObject;
import org.axonometry.models.Canvas3DModel;

public class CanvasController {
    private final Canvas3DModel model;
    private double mousePosX;
    private double mousePosY;
    private final double MOUSE_SCROLL_MULTIPLIER = 0.01;
    @FXML
    CanvasPane canvasPane;

    public CanvasController(Canvas3DModel model) {
        this.model = model;
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
            ObservableList<? extends GeometricalObject> selectedObjectsList =  observable.getList();
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
                mousePosX = event.getSceneX();
                mousePosY = event.getSceneY();
            });
            newValue.setOnMouseDragged(event -> {
                double dx = (mousePosX - event.getSceneX());
                double dy = (mousePosY - event.getSceneY());
                if (event.isPrimaryButtonDown()) {
                    model.setZRotation(model.getZRotation() - (200 * dx / bounds.getMaxY()));
                    model.setXyRotation(model.getXyRotation() - (200 * dy / bounds.getMaxX()));
                }
                mousePosX = event.getSceneX();
                mousePosY = event.getSceneY();
            });
        });

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
