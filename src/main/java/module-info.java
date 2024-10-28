module org.acsonometry {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;

    opens org.axonometry to javafx.fxml;
    exports org.axonometry;
    exports org.axonometry.geometry;
    opens org.axonometry.geometry to javafx.fxml;
    exports org.axonometry.controllers;
    opens org.axonometry.controllers to javafx.fxml;
}