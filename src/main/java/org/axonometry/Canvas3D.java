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

        Plane triangle = new Plane(new Vertex3D[]{
                new Vertex3D("A", new Vector3D(new double[][]{{100}, {0}, {0}})),
                new Vertex3D("B", new Vector3D(new double[][]{{0}, {100}, {0}})),
                new Vertex3D("C", new Vector3D(new double[][]{{0}, {0}, {100}}))
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

    public void transform(double dx, double dy, double dz, double rx, double ry, double rz, double scale) {
        for (int i = 0; i < transformedObjects.size(); i++) {
            transformedObjects.set(i, objects.get(i).transform(dx, dy, dz, rx, ry, rz, scale));
        }
    }

    public void addVertex(Vertex3D vertex) {
        objects.add(vertex);
        transformedObjects.add(vertex);
    }

    public void addPlane(ObservableList<Integer> ids) {
        Vertex3D[] vertices = new Vertex3D[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            int objectId = ids.get(i);
            vertices[i] = (Vertex3D) objects.get(objectId);
        }
        Plane plane = new Plane(vertices);
        objects.add(plane);
        transformedObjects.add(plane);
    }

    public void removeObject(ObservableList<Integer> ids) {
        for (int i = ids.size() - 1; i >= 0; i--) {
            objects.remove((int) ids.get(i));
            transformedObjects.remove((int) ids.get(i));
        }
    }

    public ArrayList<GeometricalObject> getObjects() {
        return objects;
    }
}
