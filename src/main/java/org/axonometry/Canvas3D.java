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
import java.util.Comparator;
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

        Plane Pi1 = new Plane(new Vertex3D[]{
                new Vertex3D("", new Vector3D(new double[][]{{200}, {0}, {0}})),
                new Vertex3D("", new Vector3D(new double[][]{{0}, {200}, {0}})),
                new Vertex3D("", new Vector3D(new double[][]{{0}, {0}, {0}}))
        });
        Plane Pi2 = new Plane(new Vertex3D[]{
                new Vertex3D("", new Vector3D(new double[][]{{200}, {0}, {0}})),
                new Vertex3D("", new Vector3D(new double[][]{{0}, {0}, {200}})),
                new Vertex3D("", new Vector3D(new double[][]{{0}, {0}, {0}}))
        });
        Plane Pi3 = new Plane(new Vertex3D[]{
                new Vertex3D("", new Vector3D(new double[][]{{0}, {0}, {0}})),
                new Vertex3D("", new Vector3D(new double[][]{{0}, {0}, {200}})),
                new Vertex3D("", new Vector3D(new double[][]{{0}, {200}, {0}}))
        });
        Vertex3D triangle = new Vertex3D("A", new Vector3D(new double[][]{{100}, {100}, {100}}));
        Vertex3D projection1 = triangle.projectOn(Pi1, "A'");
        Vertex3D projection2 = triangle.projectOn(Pi2, "A''");
        Vertex3D projection3 = triangle.projectOn(Pi3, "A'''");
        objects.add(new CoordinateSystem());
        objects.add(Pi1);
        objects.add(Pi2);
        objects.add(Pi3);
        objects.add(triangle);
        objects.add(projection1);
        objects.add(projection2);
        objects.add(projection3);
        transformedObjects.add(new CoordinateSystem());
        transformedObjects.add(Pi1);
        transformedObjects.add(Pi2);
        transformedObjects.add(Pi3);
        transformedObjects.add(triangle);
        transformedObjects.add(projection1);
        transformedObjects.add(projection2);
        transformedObjects.add(projection3);
    }

    public void update() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 1960, 1080);
        for (GeometricalObject object : transformedObjects) {
            object.draw(gc);
        }
    }

    public void transform(double dx, double dy, double dz, double rx, double ry, double rz, double scale) {
        IntStream.range(0, transformedObjects.size())
                .forEach(i -> transformedObjects.set(i, objects.get(i)
                        .transform(dx, dy, dz, rx, ry, rz, scale)
                ));
    }

    public void addVertex(Vertex3D vertex) {
        objects.add(vertex);
        transformedObjects.add(vertex);
    }
    public void castProjectionsFromVertex(Vertex3D vertex) {
    }

    public void addPlane(ObservableList<Integer> ids) {
        Vertex3D[] vertices = new Vertex3D[ids.size()];
        IntStream.range(0, ids.size()).forEach(i -> vertices[i] = (Vertex3D) objects.get(ids.get(i)));
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
