package org.axonometry;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.axonometry.geometry.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        this.setWidth(1960);
        this.setHeight(1080);

        Timeline timeline = new Timeline();
        KeyFrame updateLoop = new KeyFrame(Duration.seconds(0.01), e -> this.update());
        timeline.getKeyFrames().add(updateLoop);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Polygon triangle = new Polygon(new Vertex3D[]{
                new Vertex3D("A", new Coordinates(new double[][]{{100}, {0}, {0}})),
                new Vertex3D("B", new Coordinates(new double[][]{{0}, {100}, {0}})),
                new Vertex3D("C", new Coordinates(new double[][]{{0}, {0}, {100}}))
        });

        objects.add(new CoordinateSystem());
        objects.add(triangle);
        transformedObjects.add(new CoordinateSystem());
        transformedObjects.add(triangle);
    }

    public void update() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 1960, 1080);
        for (GeometricalObject object : transformedObjects) {
            object.draw(gc);
        }
    }

    public void transform(double rx, double ry, double rz, double scale) {
        for (int i = 0; i < transformedObjects.size(); i++) {
            transformedObjects.set(i, objects.get(i).transform(rx, ry, rz, scale));
        }
    }

    public void translate(double dx, double dy, double dz) {
        for (int i = 0; i < transformedObjects.size(); i++) {
            transformedObjects.set(i, objects.get(i).translate(dx, dy, dz));
        }
    }

    public void addVertex(String name, double x, double y, double z) {
        Vertex3D vertex = new Vertex3D(name, new Coordinates(new double[][]{{x}, {y}, {z}}));
        objects.add(vertex);
        transformedObjects.add(vertex);
    }

    public void addPolygon(ObservableList<Integer> ids) {
        Vertex3D[] vertices = new Vertex3D[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            int objectId = ids.get(i);
            vertices[i] = (Vertex3D) objects.get(objectId);
        }
        Polygon polygon = new Polygon(vertices);
        objects.add(polygon);
        transformedObjects.add(polygon);
    }

    public void removeObject(ObservableList<Integer> ids) {
        for (int i : ids) {
            if (objects.get(i) instanceof Polygon) {
                List<Vertex3D> vertices = Arrays.asList(((Polygon) objects.get(i)).destruct());
                objects.addAll(vertices);
                transformedObjects.addAll(vertices);
            }
            objects.remove(i);
            transformedObjects.remove(i);
        }
    }

    public ArrayList<GeometricalObject> getObjects() {
        return objects;
    }
}
