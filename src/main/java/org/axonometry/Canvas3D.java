package org.axonometry;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.axonometry.geometry.*;

import java.util.*;
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
        KeyFrame updateLoop = new KeyFrame(Duration.seconds(0.001), e -> this.update());
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

    public void addVertex(Vertex3D vertex) {
        objects.add(vertex);
        transformedObjects.add(vertex);
    }

    public void addPlane(ArrayList<Vertex3D> vertices) {
        Random random = new Random();
        Polygon polygon = new Polygon(vertices.toArray(Vertex3D[]::new), Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0.25));
        objects.add(polygon);
        transformedObjects.add(polygon);
    }

    public void removeObject(GeometricalObject object) {
        int objectId = objects.indexOf(object);
        objects.remove(objectId);
        transformedObjects.remove(objectId);
    }
    public void removeObjects(HashSet<GeometricalObject> selectedObjects) {
        ArrayList<Integer> indices = new ArrayList<>();
        selectedObjects.forEach(object -> {
            int objectId = objects.indexOf(object);
            indices.addFirst(objectId);
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
                    if (object instanceof Selectable) {
                        int objectId = objects.indexOf(object);
                        return ((Selectable) transformedObjects.get(objectId)).isClicked(x, y);
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
        objects.stream()
                .filter(object -> object instanceof Selectable)
                .forEach(object -> ((Selectable) object).setIsSelected(false));
        transformedObjects.stream()
                .filter(object -> object instanceof Selectable)
                .forEach(object -> ((Selectable) object).setIsSelected(false));
    }

    private List<GeometricalObject> sortObjectsByZValue(List<GeometricalObject> objectList) {
        return objectList.stream()
                .sorted(Comparator.comparing(object ->
                        Arrays.stream(object.getVertices())
                                .sorted(Comparator.comparing(vertex -> vertex.getCoordinates().z))
                                .toArray(Vertex3D[]::new)
                                [0].getCoordinates().z
                ))
                .toList();
    }

    public void highlightObjects(ArrayList<GeometricalObject> selectedObjects) {
        selectedObjects.forEach(object -> {
            int objectId = objects.indexOf(object);
            if (objectId == -1) {
                return;
            }
            GeometricalObject originalObject = objects.get(objectId);
            if (originalObject instanceof Selectable) {
                ((Selectable) originalObject).setIsSelected(true);
            }
            GeometricalObject transformedObject = transformedObjects.get(objectId);
            if (transformedObject instanceof Selectable) {
                ((Selectable) transformedObject).setIsSelected(true);
            }
        });
    }

    public ArrayList<GeometricalObject> getObjects() {
        return objects;
    }
}
