package org.axonometry.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.axonometry.geometry.Vertex3D;
import org.axonometry.models.Canvas3DModel;

public class TabsController {
    private final Canvas3DModel model;
    @FXML
    TextField vertexCreationField;

    public TabsController(Canvas3DModel model) {
        this.model = model;
    }

    public void initialize() {
        setHandlers();
    }

    private void setHandlers() {
        vertexCreationField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createVertex();
                vertexCreationField.clear();
            }
        });
    }

    private void createVertex() {
        Vertex3D vertex = Vertex3D.fromString(vertexCreationField.getText());
        model.getCanvas().addVertex(vertex);
        model.getSelectedObjects().add(vertex);
        model.setObjectCount(model.getObjectCount() + 1);
    }
}
