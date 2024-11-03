package org.axonometry;

import javafx.scene.layout.Region;

import java.util.function.Consumer;

public class CanvasPane extends Region {
    private final Canvas3D canvas ;
    private Consumer<Canvas3D> repaint ;
    public CanvasPane() {
        this.canvas = new Canvas3D();
        getChildren().add(canvas);
        repaint = c -> {} ;
    }

    public Consumer<Canvas3D> getRepaint() {
        return repaint;
    }

    public void setRepaint(Consumer<Canvas3D> repaint) {
        this.repaint = repaint ;
    }

    public Canvas3D getCanvas() {
        return canvas ;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        double width = getWidth();
        canvas.setWidth(width);
        double height = getHeight();
        canvas.setHeight(height);
        repaint.accept(canvas);
    }
}
