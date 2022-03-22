package fi.utu.tech.gui.javafx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Creates a ResourceLoader for every scene in the App.
 * In charge of controlling which scenes are shown and 
 * passes information from a scene to another.
 */
public class MainApp extends Application {

	private static BattleshipGame game = new BattleshipGame();
    public static BattleshipGame getGame() { return MainApp.game; };
	
    // Used to set the style for a scene
    protected String createStyle() {
        return ResourceLoader.stylesheet("styles.css");
    }

    @Override
    public void start(Stage stage) {
    	game = new BattleshipGame();
    	
    	// Loaders for every scene
        ResourceLoader<Parent, StartMenuController> startMenuLoader = new ResourceLoader<>("startMenuScene.fxml");
        ResourceLoader<Parent, setShipsSceneController> setShipsLoader = new ResourceLoader<>("setShipsScene.fxml");
        ResourceLoader<Parent, gameOverSceneController> gameOverLoader = new ResourceLoader<>("gameOverScene.fxml");
        
        Scene startMenuScene = new Scene(startMenuLoader.root);
        startMenuScene.getStylesheets().add(createStyle());
        
        // Eventhandler for changing scene from StartMenu to SetShips
        startMenuLoader.controller.getStartButton().setOnAction(e -> {
        	Scene setShipsScene = new Scene(setShipsLoader.root);
        	setShipsScene.getStylesheets().add(createStyle());
        	setShipsLoader.controller.drawBoard();
        	stage.setScene(setShipsScene);
        });
        
        // Eventhandler for changing scene from gameOver to StartMenu
        gameOverLoader.controller.getPlayAgainButton().setOnAction(e -> {
        	stage.setScene(startMenuScene);
        });

        stage.setTitle("Laivanupotus");
        stage.setScene(startMenuScene);
        stage.show();
    }
}
