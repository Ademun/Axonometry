package org.axonometry;

import javafx.scene.control.Label;
import org.axonometry.geometry.Vector3D;
import org.axonometry.geometry.Vertex3D;

import java.text.DecimalFormat;

public class Vertex3DTab extends ObjectTab{
    public Vertex3DTab(Vertex3D vertex) {
        super(vertex.getName(), vertex);
        initialize();
    }
    @Override
    public void initialize() {
        this.getStyleClass().add("tab");
        this.setText(String.format("Точка %s", getName()));
        this.setClosable(false);
        DecimalFormat df = new DecimalFormat("##.##");
        Vector3D coordinates = ((Vertex3D) getObject()).getCoordinates();
        Label coordinatesText = new Label(String.format(
                "Координаты: X: %s Y: %s Z: %s",
                df.format(coordinates.getX()),
                df.format(coordinates.getY()),
                df.format(coordinates.getZ()))
        );
        coordinatesText.getStyleClass().add("label");
        this.setContent(coordinatesText);
    }
}
