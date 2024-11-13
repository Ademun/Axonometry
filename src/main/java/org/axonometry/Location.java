package org.axonometry;

public enum Location {
    X_AXIS("Ox"),
    Y_AXIS("Oy"),
    Z_AXIS("Oz"),
    HORIZONTAL_PLANE("π1"),
    FRONTAL_PLANE("π2"),
    PROFILE_PLANE("π3"),
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
