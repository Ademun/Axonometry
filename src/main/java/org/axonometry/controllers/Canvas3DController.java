package org.axonometry.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.axonometry.ObjectTab;
import org.axonometry.Vertex3DTab;
import org.axonometry.geometry.GeometricalObject;
import org.axonometry.geometry.Vertex3D;

public class Canvas3DController {
    @FXML
    public TabPane canvasTabs;

    public void initialize() {
    }

//            canvasPane.getScene().setOnMouseClicked(event -> {
//                boolean isShiftPressed = pressedKeys.contains(KeyCode.SHIFT);
//                canvasPane.requestFocus();
//                GeometricalObject selectedObject = canvasPane.getCanvas().getClickedObject(event.getX() - 175.2, event.getY() - 25.6, !isShiftPressed);
//                if (canvasTabs.getTabs().size() > 1) {
//                    removeObjectTab();
//                }
//                if (selectedObject == null) {
//                    return;
//                }
//                if (!isShiftPressed) {
//                    selectedObjects.clear();
//                    createObjectTab(selectedObject);
//                }
//                selectedObjects.add(selectedObject);
//            });
//            canvasPane.getScene().setOnKeyPressed(event -> {
//                pressedKeys.add(event.getCode());
//                handleKeyPress();
//            });
//            canvasPane.getScene().setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
//        });
//        vertexField.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ENTER) createVertex();
//        });
//    }

//    private void handleKeyPress() {
//        pressedKeys.forEach(keyCode -> {
//            switch (keyCode) {
//                case DELETE -> deleteObjects();
//                case P -> createPlane();
//            }
//        });
//    }

//
//    private void createPlane() {
//        if (selectedObjects.size() != 3) return;
//        List<Vertex3D> selectedVertices = selectedObjects.stream()
//                .filter(object -> object instanceof Vertex3D)
//                .map(vertex -> (Vertex3D) vertex).toList();
//        canvasPane.getCanvas().addPlane(new ArrayList<>(selectedVertices));
//    }
//
//
//    private void deleteObjects() {
//        canvasPane.getCanvas().removeObjects(selectedObjects);
//        if (canvasTabs.getTabs().size() > 1) {
//            removeObjectTab();
//        }
//    }

    private void createObjectTab(GeometricalObject object) {
        ObjectTab objectTab = switch (object) {
            case Vertex3D vertex -> new Vertex3DTab(vertex);
            default -> null;
        };
        if (objectTab == null) {
            return;
        }
        canvasTabs.getTabs().add(objectTab);
        canvasTabs.getSelectionModel().select(objectTab);
    }

    private void removeObjectTab() {
        Tab objectTab = canvasTabs.getTabs().remove(1);
        canvasTabs.getTabs().remove(objectTab);
    }
}

