package org.axonometry.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import org.axonometry.CanvasPane;
import org.axonometry.geometry.GeometricalObject;
import org.axonometry.geometry.Vertex3D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Canvas3DController {
    @FXML
    public CanvasPane canvasPane;
    public TextField nameValue;
    public TextField vertexField;
    public ListView<String> objectsList;

    static public HashSet<String> pressedKeys;
    private double scale;
    private double mousePosX;
    private double mousePosY;
    private double xRotation;
    private double yRotation;
    private double zRotation;

    @FXML
    public void deleteObjects() {
        canvasPane.getCanvas().removeObject(objectsList.getSelectionModel().getSelectedIndices());
        transformCanvas();
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
        xRotation = -27;
        yRotation = 25;
        zRotation = 40;
        transformCanvas();
        setListeners();
        setObjectsList(canvasPane.getCanvas().getObjects());
        pressedKeys = new HashSet<String>();
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
                        xRotation += (400 * dx / bounds.getMaxY());
                        yRotation -= (400 * dy / bounds.getMaxX());
                        transformCanvas();
                    }
                    mousePosX = event.getSceneX();
                    mousePosY = event.getSceneY();
                });
                canvasPane.getScene().setOnScroll(event -> {
                    System.out.println(event.getDeltaY());
                    scale += event.getDeltaY() * 0.025;
                    scale = Math.max(1, Math.min(50, scale));
                    transformCanvas();
                });
                canvasPane.getScene().setOnKeyPressed(event -> {
                    System.out.println(event.getCode().toString());
                    switch (event.getCode().toString()) {
                        case "W":
                            transformCanvas();
                            break;
                        case "A":
                            transformCanvas();
                            break;
                        case "S":
                            transformCanvas();
                            break;
                        case "D":
                            transformCanvas();
                            break;
                        default:
                            break;
                    }
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
        canvasPane.getCanvas().transform(
                0, 0, 0,
                xRotation * Math.PI / 180,
                yRotation * Math.PI / 180,
                zRotation * Math.PI / 180,
                scale
        );
    }

    private void setObjectsList(ArrayList<GeometricalObject> list) {
        ObservableList<String> observableList = FXCollections.observableArrayList(list.stream().map(GeometricalObject::toString).collect(Collectors.toList()));
        objectsList.setItems(observableList);
    }
}

