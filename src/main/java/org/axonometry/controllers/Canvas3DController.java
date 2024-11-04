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
    public TextField vertexField;
    public ListView<String> objectsList;

    static public HashSet<KeyCode> pressedKeys;
    private double scale;
    private double dX;
    private double dZ;
    private double mousePosX;
    private double mousePosY;
    private double xyRotation;
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
        xyRotation = -25;
        zRotation = 500;
        transformCanvas();
        setListeners();
        setObjectsList(canvasPane.getCanvas().getObjects());
        pressedKeys = new HashSet<>();
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
                        zRotation += (400 * dx / bounds.getMaxY());
                        xyRotation -= (400 * dy / bounds.getMaxX());
                        transformCanvas();
                    }
                    mousePosX = event.getSceneX();
                    mousePosY = event.getSceneY();
                    System.out.println(zRotation);
                    System.out.println(xyRotation);
                });
                canvasPane.getScene().setOnScroll(event -> {
                    scale += event.getDeltaY() * 0.025;
                    scale = Math.max(0.5, Math.min(50, scale));
                    transformCanvas();
                });
                canvasPane.getScene().setOnKeyPressed(event -> {
                    KeyCode key = event.getCode();
                    if (key.equals(KeyCode.W)) {
                        dZ += 5;
                    }
                    if (key.equals(KeyCode.A)) {
                        dX += 5;
                    }
                    if (key.equals(KeyCode.S)) {
                        dZ -= 5;
                    }
                    if (key.equals(KeyCode.D)) {
                        dX -= 5;
                    }
                    transformCanvas();
                });
                canvasPane.getScene().setOnMouseClicked(event -> canvasPane.getCanvas().getClickedObject(event.getX() - 175.2, event.getY() - 25.6));
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
                dX, 0, dZ,
                xyRotation * Math.PI / 180,
                -1 * xyRotation * Math.PI / 180,
                -1 * zRotation * Math.PI / 180,
                scale
        );
    }

    private void setObjectsList(ArrayList<GeometricalObject> list) {
        ObservableList<String> observableList = FXCollections.observableArrayList(list.stream().map(GeometricalObject::toString).collect(Collectors.toList()));
        objectsList.setItems(observableList);
    }
}

