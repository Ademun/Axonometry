package org.axonometry;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Canvas3DView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1960, 1080);
        scene.getStylesheets().add(Application.class.getResource("Canvas3DView.css").toExternalForm());
        stage.setMaximized(true);
        stage.setTitle("Axonometry");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}