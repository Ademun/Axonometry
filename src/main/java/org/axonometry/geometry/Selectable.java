package org.axonometry.geometry;

public interface Selectable {
    //Проверяет, находится ли точка (x, y) в области фигуры
    boolean isClicked(double x, double y);

    void setIsSelected(boolean clicked);
    boolean getIsSelected();
}
