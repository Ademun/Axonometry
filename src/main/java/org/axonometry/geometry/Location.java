package org.axonometry.geometry;

public enum Location {
    CENTER("Начало координат"),
    X_AXIS("Ось x"),
    Y_AXIS("Ось y"),
    Z_AXIS("Ось z"),
    HORIZONTAL_PLANE("XY"),
    FRONTAL_PLANE("XZ"),
    PROFILE_PLANE("YZ"),
    OCTANT_1("I Октант"),
    OCTANT_2("II Октант"),
    OCTANT_3("III Октант"),
    OCTANT_4("IV Октант"),
    OCTANT_5("V Октант"),
    OCTANT_6("VI Октант"),
    OCTANT_7("VII Октант"),
    OCTANT_8("VIII Октант"),
    ;
    private final String text;
    Location(String text) {
        this.text = text;
    }
    public String toString() {
        return text;
    }
}
