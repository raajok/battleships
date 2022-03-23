package fi.utu.tech.gui.javafx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
	private static BattleshipGame game = new BattleshipGame();
	public static BattleshipGame getGame() { return MainApp.game; };
	
    protected String createStyle() {
        return ResourceLoader.stylesheet("styles.css");
    }

    @Override
    public void start(Stage stage) {
        ResourceLoader<Parent, gameSceneController> loader = new ResourceLoader<>("gameScene.fxml");


        Scene scene = new Scene(loader.root);
        scene.getStylesheets().add(createStyle());
        loader.controller.init(scene);

		// Minimum stage size
        stage.setMinWidth(600);
        stage.setMinHeight(300);
        stage.setWidth(800);
        stage.setHeight(450);
        stage.setTitle("Laivanupotus");
        stage.setScene(scene);
        stage.show();
    }
}
