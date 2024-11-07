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
        for (GeometricalObject object : transformedObjects) {
            object.draw(gc);
        }
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
        Plane plane = new Plane(vertices.toArray(Vertex3D[]::new), Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0.25));
        objects.add(plane);
        transformedObjects.add(plane);
    }
    public void removeObjects(HashSet<GeometricalObject> selectedObjects) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (GeometricalObject geometricalObject: selectedObjects) {
            int objectId = objects.indexOf(geometricalObject);
            indices.addFirst(objectId);
        }
        indices.forEach(id -> {
            objects.remove((int) id);
            transformedObjects.remove((int) id);
        });
    }
    public GeometricalObject getClickedObject(double x, double y, boolean resetSelection) {
        if (resetSelection) {
            transformedObjects.stream()
                    .filter(object -> object instanceof Clickable)
                    .forEach(object -> ((Clickable) object).setIsSelected(false));
        }
        List<GeometricalObject> clickedObjectsSorted = getClickedObjectsSorted(x, y);
        if (clickedObjectsSorted.isEmpty()) {
            return null;
        }
        return clickedObjectsSorted.getFirst();
    }

    private List<GeometricalObject> getClickedObjectsSorted(double x, double y) {
        return objects.stream()
                .filter(object -> object instanceof Clickable && ((Clickable) transformedObjects.get(objects.indexOf(object))).isClicked(x, y))
                .peek(object -> ((Clickable) transformedObjects.get(objects.indexOf(object))).setIsSelected(((Clickable) transformedObjects.get(objects.indexOf(object))).isClicked(x, y)))
                .sorted(Comparator.comparing(object ->
                        Arrays.stream(object.getVertices())
                                .sorted(Comparator.comparing(vertex -> vertex.getCoordinates().getZ()))
                                .toArray(Vertex3D[]::new)
                                [0].getCoordinates().getZ()
                ))
                .toList();
    }
    public void highlightObjects(ArrayList<GeometricalObject> objects) {
    }

    public ArrayList<GeometricalObject> getObjects() {
        return objects;
    }
}
