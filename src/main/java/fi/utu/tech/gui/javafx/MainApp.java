package fi.utu.tech.gui.javafx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    protected String createStyle() {
        return ResourceLoader.stylesheet("styles.css");
    }

    @Override
    public void start(Stage stage) {
        ResourceLoader<Parent, AlkuvalikkoController> loader = new ResourceLoader<>("startMenuScene.fxml");

        Scene scene = new Scene(loader.root);
        scene.getStylesheets().add(createStyle());

        stage.setTitle("Laivanupotus");
        stage.setScene(scene);
        stage.show();
    }
}
