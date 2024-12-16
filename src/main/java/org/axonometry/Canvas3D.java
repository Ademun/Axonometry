package org.axonometry;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.axonometry.geometry.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Canvas3D extends Canvas {
    private final GraphicsContext gc;
    private final ArrayList<GeometricalObject> objects;
    private final ArrayList<GeometricalObject> transformedObjects;

    public Canvas3D() {
        gc = this.getGraphicsContext2D();
        objects = new ArrayList<>();
        transformedObjects = new ArrayList<>();
        init();
    }

    private void init() {
        initAnimation();
        CoordinateSystem rootCoordinateSystem = new CoordinateSystem();
        objects.add(rootCoordinateSystem);
        transformedObjects.add(rootCoordinateSystem);
    }

    private void initAnimation() {
        Timeline timeline = new Timeline();
        KeyFrame updateLoop = new KeyFrame(Duration.millis(4), e -> this.update());
        timeline.getKeyFrames().add(updateLoop);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void update() {
        gc.setFill(Color.rgb(1, 4, 9));
        gc.fillRect(0, 0, 1960, 1080);
        transformedObjects.forEach(object -> object.draw(gc));
    }

    public void transform(double rx, double ry, double rz, double scale) {
        IntStream.range(0, transformedObjects.size())
                .forEach(i -> transformedObjects.set(i, objects.get(i)
                        .transform(rx, ry, rz, scale)
                ));
    }

    public void addPoint(Point3D point) {
        objects.add(point);
        transformedObjects.add(point);
    }

    public void addLine(Line3D line) {
        objects.add(line);
        transformedObjects.add(line);
    }



    private Set<GeometricalObject> getObjectsContainingPoint(Point3D point) {
        return objects.stream()
                .filter(obj -> Arrays.stream(obj.getPoints()).toList().contains(point))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public void removeObjects(Set<GeometricalObject> selectedObjects) {
        HashSet<Integer> indices = new HashSet<>();
        selectedObjects.forEach(object -> {
            int objectId = objects.indexOf(object);
            indices.add(objectId);
        });
        indices.stream().sorted(Comparator.reverseOrder()).forEach(id -> {
            objects.remove(id.intValue());
            transformedObjects.remove(id.intValue());
        });
    }

    public GeometricalObject getSelectedObject(double x, double y, boolean resetSelection) {
        if (resetSelection) {
            resetObjectsSelection();
        }
        List<GeometricalObject> selectedObjects = objects.stream()
                .filter(object -> {
                    int objectId = objects.indexOf(object);
                    GeometricalObject selectedObject = transformedObjects.get(objectId);
                    if (selectedObject instanceof Selectable selectable) {
                        return selectable.isClicked(x, y);
                    }
                    return false;
                }).toList();
        List<GeometricalObject> sortedSelectedObjects = sortObjectsByZValue(selectedObjects);
        if (sortedSelectedObjects.isEmpty()) {
            return null;
        }
        return sortedSelectedObjects.getFirst();
    }

    private void resetObjectsSelection() {
        objects.forEach(object -> {
            if (object instanceof Selectable selectable) {
                selectable.setSelected(false);
            }
        });
        transformedObjects.forEach(object -> {
            if (object instanceof Selectable selectable) {
                selectable.setSelected(false);
            }
        });
    }

    private List<GeometricalObject> sortObjectsByZValue(List<GeometricalObject> objectList) {
        return objectList.stream()
                .sorted(Comparator.comparing(object ->
                        Arrays.stream(object.getPoints())
                                .sorted(Comparator.comparing(point -> point.getCoordinates().z))
                                .toArray(GeometricalPoint[]::new)
                                [0].getCoordinates().z
                ))
                .toList();
    }

    public void highlightObjects(List<GeometricalObject> selectedObjects) {
        objects.forEach(object -> {
            if (object instanceof Selectable selectable) {
                selectable.setSelected(false);
            }
        });
        selectedObjects.forEach(object -> {
            int objectId = objects.indexOf(object);
            if (objectId == -1) {
                return;
            }
            GeometricalObject originalObject = objects.get(objectId);
            if (originalObject instanceof Selectable selectable) {
                selectable.setSelected(true);
            }
        });
    }
    public List<GeometricalObject> getObjects() {
        return objects;
    }
}
