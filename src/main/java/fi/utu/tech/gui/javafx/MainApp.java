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
    	// Loaders for every scene
        ResourceLoader<Parent, StartMenuController> startMenuLoader = new ResourceLoader<>("startMenuScene.fxml");
        ResourceLoader<Parent, setShipsSceneController> setShipsLoader = new ResourceLoader<>("setShipsScene.fxml");
        ResourceLoader<Parent, gameSceneController> gameLoader = new ResourceLoader<>("gameScene.fxml");
        ResourceLoader<Parent, gameOverSceneController> gameOverLoader = new ResourceLoader<>("gameOverScene.fxml");
        
        // Scenes
        Scene startMenuScene = new Scene(startMenuLoader.root);
        Scene setShipsScene = new Scene(setShipsLoader.root);
        Scene gameScene = new Scene(gameLoader.root);
        Scene gameOverScene = new Scene(gameOverLoader.root);
        startMenuScene.getStylesheets().add(createStyle());
        setShipsScene.getStylesheets().add(createStyle());
        gameScene.getStylesheets().add(createStyle());
        gameOverScene.getStylesheets().add(createStyle());
        
        // Eventhandler for changing scene from StartMenu to SetShips
        startMenuLoader.controller.getStartButton().setOnAction(e -> {
        	setShipsLoader.controller.drawBoard();
        	stage.setScene(setShipsScene);
        });
        
        // Eventhandler for changing scene from SetShips to Game
        
        setShipsLoader.controller.getEndPlacementButton().setOnAction(e -> {
        	stage.setScene(gameScene);
        }); 
        
        
        // Eventhandler for changing scene from gameOver to StartMenu
        gameOverLoader.controller.getPlayAgainButton().setOnAction(e -> {
        	stage.setScene(startMenuScene);
        });
        
        // Initialize controllers
        gameLoader.controller.init(gameScene);
        
        // Add method to execute when game ends
        game.setOnGameEndAction(() -> {
    		stage.setScene(gameOverScene);
    	});

		// Minimum stage size
        stage.setMinWidth(600);
        stage.setMinHeight(300);
        stage.setTitle("Laivanupotus");
        stage.setScene(gameScene);
        stage.show();
    }
}
