package org.axonometry;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.axonometry.controllers.CanvasController;
import org.axonometry.controllers.StatusBarController;
import org.axonometry.controllers.TabsController;
import org.axonometry.models.Canvas3DModel;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Canvas3DModel canvas3DModel = new Canvas3DModel();

        FXMLLoader loader = new FXMLLoader(Application.class.getResource("view/StatusBar.fxml"));
        StatusBarController statusBarController = new StatusBarController(canvas3DModel);
        loader.setController(statusBarController);
        HBox statusBar = loader.load();

        loader = new FXMLLoader(Application.class.getResource("view/Canvas3D.fxml"));
        CanvasController canvasController = new CanvasController(canvas3DModel);
        loader.setController(canvasController);
        CanvasPane canvasPane = loader.load();

        loader = new FXMLLoader(Application.class.getResource("view/Tabs.fxml"));
        TabsController tabsController = new TabsController(canvas3DModel);
        loader.setController(tabsController);
        TabPane tabPane = loader.load();

        loader = new FXMLLoader(Application.class.getResource("view/Canvas3DView.fxml"));
        BorderPane root = loader.load();
        root.setCenter(canvasPane);
        root.setLeft(tabPane);
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 1960, 1080);
        scene.getStylesheets().add(Application.class.getResource("css/Canvas3DView.css").toExternalForm());
        stage.setMaximized(true);
        stage.setTitle("Axonometry");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}