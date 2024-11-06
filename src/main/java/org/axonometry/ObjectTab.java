package org.axonometry;

import javafx.scene.control.Tab;
import org.axonometry.geometry.GeometricalObject;

public abstract class ObjectTab extends Tab {
    private final String name;

    private final GeometricalObject object;
    public ObjectTab(String name, GeometricalObject object) {
        this.name = name;
        this.object = object;
    }
    public abstract void initialize();

    public String getName() {
        return name;
    }

    public GeometricalObject getObject() {
        return object;
    }
}
