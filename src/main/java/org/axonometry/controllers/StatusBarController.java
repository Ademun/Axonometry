package org.axonometry.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.axonometry.models.Canvas3DModel;

public class StatusBarController {
    private final Canvas3DModel model;
    @FXML
    public Label objectCount;
    public Label rotation;

    public StatusBarController(Canvas3DModel model) {
        this.model = model;
    }

    public void initialize() {
        objectCount.setText(String.format("Объекты: %d", model.getObjectCount()));
        rotation.setText(String.format(
                "X: %d° Y: %d° Z: %d°",
                (int) model.getXyRotation(),
                (int) model.getXyRotation(),
                (int) model.getZRotation())
        );
        setListeners();
    }

    private void setListeners() {
        model.objectCountProperty().addListener((property, oldValue, newValue) -> {
            objectCount.setText(String.format("Объекты: %d", (int) newValue));
        });
    }
}
