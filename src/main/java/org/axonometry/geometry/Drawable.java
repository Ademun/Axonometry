package org.axonometry.geometry;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
    //В реализациях этого метода координаты x и z инвертируются и смещаются для перемещения центра отрисовки в центр холста
    void draw(GraphicsContext gc);
}

