package org.axonometry.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import org.axonometry.CanvasPane;
import org.axonometry.geometry.GeometricalObject;
import org.axonometry.geometry.Vertex3D;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Canvas3DController {
    @FXML
    public CanvasPane canvasPane;
    public TextField vertexField;
    public Label objectCount;
    public Label rotation;
    public ListView<String> objectsList;
    private ArrayList<GeometricalObject> selectedObjects;
    private double scale;
    private double mousePosX;
    private double mousePosY;
    private double xyRotation;
    private double zRotation;

    @FXML
    public void deleteObjects() {
        canvasPane.getCanvas().removeObjects(selectedObjects);
        setObjectsList(canvasPane.getCanvas().getObjects());
    }
    @FXML
    public void createPlane() {
        canvasPane.getCanvas().addPlane(objectsList.getSelectionModel().getSelectedIndices());
        transformCanvas();
        setObjectsList(canvasPane.getCanvas().getObjects());
    }


    public void initialize() {
        scale = 1;
        xyRotation = -10;
        zRotation = -44;
        transformCanvas();
        setListeners();
        selectedObjects = new ArrayList<>();
        setObjectsList(canvasPane.getCanvas().getObjects());
        objectsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
                    canvasPane.requestFocus();
                    GeometricalObject selectedObject = canvasPane
                            .getCanvas()
                            .getClickedObject(event.getX() - 175.2, event.getY() - 25.6);
                    if (selectedObject != null) {
                        selectedObjects.add(selectedObject);
                    } else {
                        selectedObjects = new ArrayList<>();
                    }
                });
                canvasPane.getScene().setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.DELETE) deleteObjects();
                });
            }
        });
        vertexField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) createVertex();
        });
    }

    private void createVertex() {
        canvasPane.getCanvas().addVertex(Vertex3D.fromString(vertexField.getText()));
        transformCanvas();
        setObjectsList(canvasPane.getCanvas().getObjects());
    }

    private void transformCanvas() {
        rotation.setText("X: " + Math.round(xyRotation) + " Y: " + Math.round(xyRotation) + " Z: " + Math.round(zRotation));
        canvasPane.getCanvas().transform(
                xyRotation * Math.PI / 180,
                -1 * xyRotation * Math.PI / 180,
                -1 * zRotation * Math.PI / 180,
                scale
        );
    }

    private void setObjectsList(ArrayList<GeometricalObject> list) {
        ObservableList<String> observableList = FXCollections.observableArrayList(list.stream().map(GeometricalObject::toString).collect(Collectors.toList()));
        objectsList.setItems(observableList);
        objectCount.setText("Объекты: " + (canvasPane.getCanvas().getObjects().size() - 1));
    }
}

