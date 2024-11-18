package org.axonometry.controllers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.axonometry.Application;
import org.axonometry.geometry.GeometricalObject;
import org.axonometry.geometry.Vertex3D;
import org.axonometry.models.Canvas3DModel;

import java.io.IOException;

public class TabPanelController {
    private final Canvas3DModel model;
    @FXML
    TabPane tabPanel;
    @FXML
    TextField vertexCreationField;

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
        vertexCreationField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createVertexFromString(vertexCreationField.getText());
                vertexCreationField.clear();
            }
        });
    }

    private void addObjectTab(GeometricalObject object) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        switch (object) {
            case Vertex3D vertex -> {
                Vertex3DTabController controller = new Vertex3DTabController(model, vertex);
                loader.setLocation(Application.class.getResource("view/tabs/Vertex3DTab.fxml"));
                loader.setController(controller);
            }
            default -> {
                loader.setLocation(Application.class.getResource(""));
            }
        }
        Tab objectTab = loader.load();
        tabPanel.getTabs().add(objectTab);
        tabPanel.getSelectionModel().select(objectTab);
    }

    private void createVertexFromString(String vertexString) {
        Vertex3D vertex = Vertex3D.fromString(vertexString);
        model.getCanvas().addVertex(vertex);
        model.setObjectCount(model.getObjectCount() + 1);
        model.getSelectedObjects().clear();
        model.getSelectedObjects().add(vertex);
    }
}
