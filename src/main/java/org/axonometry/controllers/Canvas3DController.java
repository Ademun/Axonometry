package org.axonometry.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import org.axonometry.CanvasPane;
import org.axonometry.geometry.GeometricalObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Canvas3DController {
    @FXML
    public CanvasPane canvasPane;
    public Slider xSlider;
    public Slider ySlider;
    public Slider zSlider;
    public Slider scaleSlider;
    public TextField nameValue;
    public TextField xValue;
    public TextField yValue;
    public TextField zValue;
    public ListView<String> objectsList;

    static public HashSet<String> pressedKeys;

    @FXML
    public void createVertex() {
        canvasPane.getCanvas().addVertex(
                nameValue.getText(),
                Double.parseDouble(xValue.getText()),
                Double.parseDouble(yValue.getText()),
                Double.parseDouble(zValue.getText())
        );
        transformCanvas();
        setObjectsList(canvasPane.getCanvas().getObjects());
    }

    @FXML
    public void deleteObjects() {
        canvasPane.getCanvas().removeObject(objectsList.getSelectionModel().getSelectedIndices());
        transformCanvas();
        setObjectsList(canvasPane.getCanvas().getObjects());
    }

    @FXML
    public void createPolygon() {
        canvasPane.getCanvas().addPolygon(objectsList.getSelectionModel().getSelectedIndices());
        transformCanvas();
        setObjectsList(canvasPane.getCanvas().getObjects());
    }


    public void initialize() {
        defaultAngle();
        setListeners();
        setObjectsList(canvasPane.getCanvas().getObjects());
        pressedKeys = new HashSet<String>();
        objectsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void defaultAngle() {
        xSlider.setValue(340);
        ySlider.setValue(20);
        zSlider.setValue(45);
        transformCanvas();
    }

    private void setListeners() {
        canvasPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                canvasPane.getScene().setOnKeyPressed(event -> {
                    System.out.println(event.getCode().toString());
                    switch (event.getCode().toString()) {
                        case "UP":
                            canvasPane.getCanvas().translate(0, 0, 1);
                            break;
                        case "RIGHT":
                            canvasPane.getCanvas().translate(-1, 0, 0);
                            break;
                        case "DOWN":
                            canvasPane.getCanvas().translate(0, 0, -1);
                            break;
                        case "LEFT":
                            canvasPane.getCanvas().translate(1, 0, 0);
                            break;
                        default:
                            break;
                    }
                });
            }
        });
        xSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            transformCanvas();
        });
        ySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            transformCanvas();
        });
        zSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            transformCanvas();
        });
        scaleSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            transformCanvas();
        });
    }

    private void transformCanvas() {
        canvasPane.getCanvas().transform(
                xSlider.getValue() * Math.PI / 180,
                ySlider.getValue() * Math.PI / 180,
                zSlider.getValue() * Math.PI / 180,
                scaleSlider.getValue()
        );
    }

    private void setObjectsList(ArrayList<GeometricalObject> list) {
        ObservableList<String> observableList = FXCollections.observableArrayList(list.stream().map(GeometricalObject::toString).collect(Collectors.toList()));
        objectsList.setItems(observableList);
    }
}

