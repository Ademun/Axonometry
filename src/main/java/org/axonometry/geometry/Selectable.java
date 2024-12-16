package org.axonometry.geometry;

public interface Selectable {
    //Проверяет, находится ли точка (x, z) в области фигуры
    boolean isClicked(double x, double z);

    void setSelected(boolean clicked);
    boolean getSelected();
}
