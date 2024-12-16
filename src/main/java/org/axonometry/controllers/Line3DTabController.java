package org.axonometry.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import org.axonometry.geometry.GeometricalObject;
import org.axonometry.geometry.Line3D;
import org.axonometry.models.Canvas3DModel;
import org.controlsfx.control.ToggleSwitch;

import java.util.HashSet;

public class Line3DTabController {
    private final Canvas3DModel model;
    private final Line3D line;
    @FXML
    Tab lineTab;
    @FXML
    Label titleLabel;
    @FXML
    Label lengthLabel;
    @FXML
    Label vectorLabel;
    @FXML
    ToggleSwitch projectionsToggle;
    @FXML
    Button deleteButton;

    public Line3DTabController(Canvas3DModel model, Line3D line) {
        this.model = model;
        this.line = line;
    }

    public void initialize() {
        setListeners();
        setHandlers();
        titleLabel.setText("Информация о линии " + line.getName());
        lengthLabel.setText(String.valueOf(((float) line.getLength())));
        vectorLabel.setText(line.getDirectionVector().toString());
        projectionsToggle.setSelected(line.isProjecting());
    }

    public void setHandlers() {
        deleteButton.setOnAction(event -> handleLineDeletion());
    }

    private void setListeners() {
        projectionsToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            line.setProjecting(newValue);
            model.transformCanvas();
        });
    }


    private void handleLineDeletion() {
        model.getSelectedObjects().clear();
        HashSet<GeometricalObject> temp = new HashSet<>();
        temp.add(line);
        model.getCanvas().removeObjects(temp);
    }

}
