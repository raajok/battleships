package fi.utu.tech.gui.javafx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

	private BattleshipGame game;
	
    protected String createStyle() {
        return ResourceLoader.stylesheet("styles.css");
    }

    @Override
    public void start(Stage stage) {
    	game = new BattleshipGame();
    	
        ResourceLoader<Parent, StartMenuController> startMenuLoader = new ResourceLoader<>("startMenuScene.fxml");
        ResourceLoader<Parent, setShipsSceneController> setShipsLoader = new ResourceLoader<>("setShipsScene.fxml");
        
        startMenuLoader.controller.setGame(game);
        startMenuLoader.controller.getStartButton().setOnAction(e -> {
        	Scene setShipsScene = new Scene(setShipsLoader.root);
        	setShipsScene.getStylesheets().add(createStyle());
        	
        	stage.setScene(setShipsScene);
        });

        Scene startMenuScene = new Scene(startMenuLoader.root);
        startMenuScene.getStylesheets().add(createStyle());

        stage.setTitle("Laivanupotus");
        stage.setScene(startMenuScene);
        stage.show();
    }
}
