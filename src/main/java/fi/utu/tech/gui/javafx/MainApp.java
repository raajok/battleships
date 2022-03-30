package fi.utu.tech.gui.javafx;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
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
        ResourceLoader<Parent, setShipsSceneController> setShipsLoader1 = new ResourceLoader<>("setShipsScene.fxml");
        ResourceLoader<Parent, setShipsSceneController> setShipsLoader2 = new ResourceLoader<>("setShipsScene.fxml");
        ResourceLoader<Parent, gameSceneController> gameLoader = new ResourceLoader<>("gameScene.fxml");
        ResourceLoader<Parent, gameOverSceneController> gameOverLoader = new ResourceLoader<>("gameOverScene.fxml");
        
        // Scenes
        Scene startMenuScene = new Scene(startMenuLoader.root);
        Scene setShipsScene1 = new Scene(setShipsLoader1.root);
        Scene setShipsScene2 = new Scene(setShipsLoader2.root);
        Scene gameScene = new Scene(gameLoader.root);
        Scene gameOverScene = new Scene(gameOverLoader.root);
        startMenuScene.getStylesheets().add(createStyle());
        setShipsScene1.getStylesheets().add(createStyle());
        setShipsScene2.getStylesheets().add(createStyle());
        gameScene.getStylesheets().add(createStyle());
        gameOverScene.getStylesheets().add(createStyle());
        
        // Eventhandler for changing scene from StartMenu to SetShips
        startMenuLoader.controller.getStartButton().setOnAction(e -> {
        	startMenuLoader.controller.startGame();
        	setShipsLoader1.controller.drawBoard();
        	stage.setScene(setShipsScene1);
        	
        	// Set stage in the center of the screen
        	Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        });
        
        // Eventhandler for changing scene from SetShips1 to setShips2
        setShipsLoader1.controller.getEndPlacementButton().setOnAction(e -> {

    		game.switchTurn();
    		setShipsLoader2.controller.drawBoard();
    		stage.setScene(setShipsScene2);

        });
        
        // Eventhandler for changing scene from SetShips2 to Game
        setShipsLoader2.controller.getEndPlacementButton().setOnAction(e -> {
        	
        	game.switchTurn();

        	// Initialize the game scene controller
    		gameLoader.controller.init(gameScene);
    		stage.setScene(gameScene);
    		
    		// Set stage in the center of the screen
    		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    		stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
    		stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);        		
        }); 
        
        // Eventhandler for changing scene from gameOver to StartMenu
        gameOverLoader.controller.getPlayAgainButton().setOnAction(e -> {
        	stage.setScene(startMenuScene);
        });
        
        // Add a method to execute when game ends
        game.setOnGameEndAction(() -> {
    		stage.setScene(gameOverScene);
    	});

		// Minimum stage size
        stage.setMinWidth(600);
        stage.setMinHeight(300);
        stage.setTitle("Laivanupotus");
        stage.setScene(startMenuScene);
        stage.show();
    }
}
