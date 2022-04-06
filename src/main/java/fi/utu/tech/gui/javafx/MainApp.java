package fi.utu.tech.gui.javafx;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
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
    private ResourceLoader<Parent, StartMenuController> startMenuLoader;
    private ResourceLoader<Parent, setShipsSceneController> setShipsLoader1;
    private ResourceLoader<Parent, setShipsSceneController> setShipsLoader2;
    private ResourceLoader<Parent, gameSceneController> gameLoader;
    private ResourceLoader<Parent, gameOverSceneController> gameOverLoader;
    private ResourceLoader<Parent, SoundBoxController> soundBoxLoader;
    private Scene startMenuScene;
    private Scene setShipsScene1;
    private Scene setShipsScene2;
    private Scene gameScene;
    private Scene gameOverScene;
    private Stage stage;
    private Group soundBoxContainer;
	
    // Used to set the style for a scene
    protected String createStyle() {
        return ResourceLoader.stylesheet("styles.css");
    }

    @Override
    public void start(Stage stage) {
    	
    	this.stage = stage;
    	initScenes();
    	
		// Minimum stage size
        this.stage.setMinWidth(600);
        this.stage.setMinHeight(300);
        this.stage.setTitle("Laivanupotus");
        this.stage.setScene(startMenuScene);
        this.stage.show();
        
    }
    
    private void initScenes() {
    	// Loaders for every scene
        startMenuLoader = new ResourceLoader<>("startMenuScene.fxml");
        setShipsLoader1 = new ResourceLoader<>("setShipsScene.fxml");
        setShipsLoader2 = new ResourceLoader<>("setShipsScene.fxml");
        gameLoader = new ResourceLoader<>("gameScene.fxml");
        gameOverLoader = new ResourceLoader<>("gameOverScene.fxml");
        soundBoxLoader = new ResourceLoader<>("SoundBoxScene.fxml");
        
        // Scenes
        startMenuScene = new Scene(startMenuLoader.root);
        setShipsScene1 = new Scene(setShipsLoader1.root);
        setShipsScene2 = new Scene(setShipsLoader2.root);
        gameScene = new Scene(gameLoader.root);
        gameOverScene = new Scene(gameOverLoader.root);
        soundBoxContainer = new Group(soundBoxLoader.root);
        startMenuScene.getStylesheets().add(createStyle());
        setShipsScene1.getStylesheets().add(createStyle());
        setShipsScene2.getStylesheets().add(createStyle());
        gameScene.getStylesheets().add(createStyle());
        gameOverScene.getStylesheets().add(createStyle());
        soundBoxContainer.getStylesheets().add(createStyle());
        
        // Set Sound Box on each scene
        startMenuLoader.controller.setSoundBox(soundBoxContainer);
        
        // Initialize sound box
        soundBoxLoader.controller.init();
        
        // Eventhandler for changing scene from StartMenu to SetShips
        startMenuLoader.controller.getStartButton().setOnAction(e -> {

    		// If player names are empty, then use these defaults
        	if (game.getPlayerNamesProperty()[Player.PLAYER1.ordinal()].getValue() == "")
        		game.getPlayerNamesProperty()[Player.PLAYER1.ordinal()].setValue("Pelaaja 1");
        	if (game.getPlayerNamesProperty()[Player.PLAYER2.ordinal()].getValue() == "")
        		game.getPlayerNamesProperty()[Player.PLAYER2.ordinal()].setValue("Pelaaja 2");
        	
        	// Player 1 sets the ships first
        	game.playerInTurnProperty().set(Player.PLAYER1);
        	
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
    		
    		// Set stage in the center of the screen
    		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    		stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
    		stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

        });
        
        // Eventhandler for changing scene from SetShips2 to Game
        setShipsLoader2.controller.getEndPlacementButton().setOnAction(e -> {
        	
        	game.switchTurn();

        	// Initialize the game scene controller
    		gameLoader.controller.init(gameScene);
            gameLoader.controller.setSoundBox(soundBoxContainer);
    		stage.setScene(gameScene);
    		
    		// Set stage in the center of the screen
    		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    		stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
    		stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);        		
        }); 
        
        // Eventhandler for changing scene from gameOver to StartMenu
        gameOverLoader.controller.getPlayAgainButton().setOnAction(e -> {
        	stage.setScene(startMenuScene);
        	// Set stage in the center of the screen
    		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    		stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
    		stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);   
        });
        
        // Add a method to execute when game ends
        game.setOnGameEndAction(() -> {
        	// stop any ongoing music
        	soundBoxLoader.controller.stop();
        	stage.setScene(gameOverScene);
        	
        	// Set stage in the center of the screen
        	Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        	stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        	stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);   
    	});
        
        gameOverLoader.controller.getPlayAgainButton().setOnAction(e -> {
        	MainApp.game = new BattleshipGame();
        	initScenes();
            this.stage.setScene(startMenuScene);
            
            // Set stage in the center of the screen
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);   
            
        });
    }
}
