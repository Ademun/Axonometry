package org.axonometry;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
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
        Timeline timeline = new Timeline();
        KeyFrame updateLoop = new KeyFrame(Duration.seconds(0.001), e -> this.update());
        timeline.getKeyFrames().add(updateLoop);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        CoordinateSystem rootCoordinateSystem = new CoordinateSystem();
        objects.add(rootCoordinateSystem);
        transformedObjects.add(rootCoordinateSystem);
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

    public void addPlane(ObservableList<Integer> ids) {
        Random random = new Random();
        Vertex3D[] vertices = new Vertex3D[ids.size()];
        IntStream.range(0, ids.size()).forEach(i -> vertices[i] = (Vertex3D) objects.get(ids.get(i)));
        Plane plane = new Plane(vertices, Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0.25));
        objects.add(plane);
        transformedObjects.add(plane);
    }
    public void removeObjects(ArrayList<GeometricalObject> selectedObjects) {
        objects.removeAll(selectedObjects);
        transformedObjects.removeAll(selectedObjects);
    }

    public GeometricalObject getClickedObject(double x, double y) {
        return transformedObjects.stream()
                .filter(object -> object instanceof Vertex3D)
                .peek(object -> ((Vertex3D) object).setIsSelected(((Vertex3D) object).isClicked(x, y)))
                .sorted(Comparator.comparing(object ->
                        Arrays.stream(object.getVertices())
                                .sorted(Comparator.comparing(vertex -> vertex.getCoordinates().getZ()))
                                .toArray(Vertex3D[]::new)
                                [0].getCoordinates().getZ()
                ))
                .toList().get(0);
    }
    public ArrayList<GeometricalObject> getObjects() {
        return objects;
    }
}
