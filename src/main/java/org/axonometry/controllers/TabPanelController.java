package org.axonometry.controllers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.axonometry.Application;
import org.axonometry.geometry.GeometricalObject;
import org.axonometry.geometry.Line3D;
import org.axonometry.geometry.Point3D;
import org.axonometry.models.Canvas3DModel;

import java.io.IOException;

public class TabPanelController {
    private final Canvas3DModel model;
    @FXML
    TabPane tabPanel;
    @FXML
    TextField pointCreationField;

    public TabPanelController(Canvas3DModel model) {
        this.model = model;
    }

    public void initialize() {
        setListeners();
        setHandlers();
    }

    private void setListeners() {
        model.getSelectedObjects().addListener((ListChangeListener<? super GeometricalObject>) observable -> {
            var selectedObjectsList = observable.getList();
            if (tabPanel.getTabs().size() > 1) {
                tabPanel.getTabs().remove(1);
            }
            if (!selectedObjectsList.isEmpty()) {
                try {
                    addObjectTab(selectedObjectsList.getLast());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void setHandlers() {
        pointCreationField.setOnKeyPressed(event -> handlePointCreation(event));
    }

    private void handlePointCreation(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) {
            return;
        }
        createPointFromString(pointCreationField.getText());
        pointCreationField.clear();
    }

    private void addObjectTab(GeometricalObject object) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        switch (object) {
            case Point3D point -> {
                Point3DTabController controller = new Point3DTabController(model, point);
                loader.setLocation(Application.class.getResource("view/tabs/Point3DTab.fxml"));
                loader.setController(controller);
            }
            case Line3D line -> {
                Line3DTabController controller = new Line3DTabController(model, line);
                loader.setLocation(Application.class.getResource("view/tabs/Line3DTab.fxml"));
                loader.setController(controller);
            }
            default -> {
                return;
            }
        }
        Tab objectTab = loader.load();
        tabPanel.getTabs().add(objectTab);
        tabPanel.getSelectionModel().select(objectTab);
    }

    private void createPointFromString(String pointString) {
        Point3D point = Point3D.fromString(pointString);
        model.getCanvas().addPoint(point);
        model.setObjectCount(model.getObjectCount() + 1);
        model.getSelectedObjects().clear();
        model.getSelectedObjects().add(point);
    }
}
