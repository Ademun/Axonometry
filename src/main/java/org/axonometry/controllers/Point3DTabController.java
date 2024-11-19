package org.axonometry.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import org.axonometry.geometry.Point3D;
import org.axonometry.geometry.Projection;
import org.axonometry.models.Canvas3DModel;
import org.controlsfx.control.ToggleSwitch;

public class Point3DTabController {
    private final Canvas3DModel model;
    private final Point3D point;
    @FXML
    Tab pointTab;
    @FXML
    Label titleLabel;
    @FXML
    Label coordinatesLabel;
    @FXML
    Label locationLabel;
    @FXML
    Label XYProjectionLabel;
    @FXML
    Label XZProjectionLabel;
    @FXML
    Label YZProjectionLabel;
    @FXML
    ToggleSwitch projectionsToggle;
    @FXML
    Button deleteButton;

    public Point3DTabController(Canvas3DModel model, Point3D point) {
        this.model = model;
        this.point = point;
    }
    public void initialize() {
        setListeners();
        setHandlers();
        titleLabel.setText("Информация о точке " + point.getName());
        coordinatesLabel.setText(point.getCoordinates().toString());
        locationLabel.setText(point.getLocation().toString());
        Projection[] projections = point.getProjections();
        XYProjectionLabel.setText(String.format("XY %s", projections[0].getCoordinates().toString()));
        XZProjectionLabel.setText(String.format("XZ %s", projections[1].getCoordinates().toString()));
        YZProjectionLabel.setText(String.format("YZ %s", projections[2].getCoordinates().toString()));
        projectionsToggle.setSelected(true);
    }

    public void setHandlers() {
        deleteButton.setOnAction(event -> handlePointDeletion());
    }

    private void setListeners() {
        projectionsToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            point.setEnableProjections(newValue);
            model.transformCanvas();
        });
    }


    private void handlePointDeletion() {
        model.getSelectedObjects().clear();
        model.getCanvas().removeObject(point);
    }

}
