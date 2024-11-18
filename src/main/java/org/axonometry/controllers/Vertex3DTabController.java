package org.axonometry.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import org.axonometry.geometry.Vector3D;
import org.axonometry.geometry.Vertex3D;
import org.axonometry.models.Canvas3DModel;

import java.text.DecimalFormat;

public class Vertex3DTabController {
    private final Canvas3DModel model;
    private final Vertex3D vertex;
    @FXML
    Tab vertexTab;
    @FXML
    Label titleLabel;
    @FXML
    Label coordinatesLabel;
    @FXML
    Label locationLabel;
    @FXML
    Button deleteButton;

    public Vertex3DTabController(Canvas3DModel model, Vertex3D vertex) {
        this.model = model;
        this.vertex = vertex;
    }
    public void initialize() {
        setHandlers();
        titleLabel.setText("Информация о точке " + vertex.getName());
        DecimalFormat df = new DecimalFormat("##.##");
        Vector3D coordinates = vertex.getCoordinates();
        String coordinatesText = String.format(
                "(%s, %s, %s)",
                df.format(coordinates.x),
                df.format(coordinates.y),
                df.format(coordinates.z)
        );
        coordinatesLabel.setText(coordinatesText);
        locationLabel.setText(vertex.getLocation().toString());
    }

    public void setHandlers() {
        deleteButton.setOnAction(event -> deleteVertex());
    }

    private void deleteVertex() {
        model.getSelectedObjects().clear();
        model.getCanvas().removeObject(vertex);
    }
}
