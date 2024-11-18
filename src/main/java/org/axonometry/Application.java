package org.axonometry;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.axonometry.controllers.Canvas3DController;
import org.axonometry.controllers.StatusPanelController;
import org.axonometry.controllers.TabPanelController;
import org.axonometry.models.Canvas3DModel;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Canvas3DModel canvas3DModel = new Canvas3DModel();

        FXMLLoader loader = new FXMLLoader(Application.class.getResource("view/StatusPanel.fxml"));
        StatusPanelController statusPanelController = new StatusPanelController(canvas3DModel);
        loader.setController(statusPanelController);
        HBox statusBar = loader.load();

        loader = new FXMLLoader(Application.class.getResource("view/Canvas3D.fxml"));
        Canvas3DController canvas3DController = new Canvas3DController(canvas3DModel);
        loader.setController(canvas3DController);
        CanvasPane canvasPane = loader.load();

        loader = new FXMLLoader(Application.class.getResource("view/TabPanel.fxml"));
        TabPanelController tabPanelController = new TabPanelController(canvas3DModel);
        loader.setController(tabPanelController);
        TabPane tabPane = loader.load();

        loader = new FXMLLoader(Application.class.getResource("view/MainView.fxml"));
        BorderPane root = loader.load();
        root.setCenter(canvasPane);
        root.setLeft(tabPane);
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 1960, 1080);
        stage.setMaximized(true);
        stage.setTitle("Axonometry");
        stage.getIcons().add(new Image(String.valueOf(Application.class.getResource("icon.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}