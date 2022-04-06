package fi.utu.tech.gui.javafx;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class gameSceneController {
	private BattleshipGame game = MainApp.getGame();
	private int gameboardSize;
	private Alert changePlayerAlert = new Alert(AlertType.INFORMATION);
	private GameboardGUIComponent gameboardGUI1;
	private GameboardGUIComponent gameboardGUI2;
	private Group soundBoxContainer = new Group();
	
	@FXML
	private VBox rootBox;
	
	@FXML
	private Text turnInfoText;

	@FXML
	private Label player1NameText;
	
	@FXML
	private Label player2NameText;
	
	@FXML
	private Button switchTurn1Btn;
	
	@FXML
	private Button switchTurn2Btn;
	
	@FXML
	private VBox headerBox;
	
	@FXML
	private VBox gameboardVBox1;
	
	@FXML
	private VBox gameboardVBox2;
	
	@FXML
	private AnchorPane playerInfoPane;
	
	@FXML
	private StackPane gridStack1;
	
	@FXML
	private StackPane gridStack2;
	
	
	@FXML
	private StackPane mainStackPane;
	
	
	@FXML
	private void initialize() {}
	
	/**
	 * The gameSceneController constructor is binding the "turnIsOverProperty" to the "switchTurn1Btn" and "switchTurn2Btn".
	 * If the turn is over, the code will disable the button that is not the player's turn.
	 * If the turn is not over, both buttons will be disabled.
	 * 
	 */
	public gameSceneController() {
		// Bindings for Turn Switching buttons
		game.turnIsOverProperty().addListener((obj, oldVal, newVal) -> {
			if (newVal == true) {
				if (game.playerInTurnValueProperty().get() == 0) {
					switchTurn1Btn.setDisable(true);
					switchTurn2Btn.setDisable(false);
				} else if (game.playerInTurnValueProperty().get() == 1) {
					switchTurn1Btn.setDisable(false);
					switchTurn2Btn.setDisable(true);
				}
			} else {
				switchTurn1Btn.setDisable(true);
				switchTurn2Btn.setDisable(true);
			}
		});

	}
	
	/**
	 * This method initializes the game board for a two player game. It sets up the game board size,
	 * creates bindings for the game board elements, and sets up event handlers for the game board buttons.
	 * 
	 * @param scene The scene where game play is rendered.
	 * 
	 */
	
	public void init(Scene scene) {
		
		// Note: the game.newGame() method has to be called before this is executed. It should be called before leaving the setShipsScene.
		
		// Set background color
		this.rootBox.setStyle("-fx-background-color: lightsteelblue;");

		// Get the board size from the game object
		gameboardSize = this.game.boardSizeProperty().get();
		
		// Create window binding
		final int windowPadding = 50;
		final int prefWindowSize = 600 + windowPadding;
		NumberBinding scaleBinding = Bindings.min(scene.widthProperty().divide(2).divide(prefWindowSize),
				scene.heightProperty()
				.subtract(headerBox.heightProperty())
				.divide(prefWindowSize));
		
		//Gameboard 1
		gameboardGUI1 = new GameboardGUIComponent(gameboardSize, Player.PLAYER1);
		Group group1 = new Group(gameboardGUI1);
		gridStack1.translateYProperty().bind(gridStack1.getScene()
				.heightProperty()
				.subtract(headerBox.heightProperty())
				.subtract(gridStack1.heightProperty())
				.divide(2));
		gameboardGUI1.scaleProperty().bind(scaleBinding);
		gameboardGUI1.infoTextProperty().addListener((obj, oldVal, newVal) -> {
			turnInfoText.setText(newVal);
		});
		gameboardGUI1.createShipImages(game.getShips(game.getPlayerInTurn()));
		gridStack1.getChildren().add(group1);
		
		// Gameboard 2
		gameboardGUI2 = new GameboardGUIComponent(gameboardSize, Player.PLAYER2);
		Group group2 = new Group(gameboardGUI2);
		gridStack2.translateYProperty().bind(gridStack2.getScene()
				.heightProperty()
				.subtract(headerBox.heightProperty())
				.subtract(gridStack2.heightProperty())
				.divide(2));
		gameboardGUI2.scaleProperty().bind(scaleBinding);
		gameboardGUI2.infoTextProperty().addListener((obj, oldVal, newVal) -> {
			turnInfoText.setText(newVal);
		});
		gameboardGUI2.createShipImages(game.getShips(game.getOpponent()));
		gridStack2.getChildren().add(group2);
		
		// Bindings for Turn Switching buttons
		game.requestTurnChangeProperty().addListener((obj, oldVal, newVal) -> {
			if (newVal == true) {
				if (game.playerInTurnValueProperty().get() == 1) {
					switchTurn1Btn.setDisable(true);
					switchTurn2Btn.setDisable(false);
				} else if (game.playerInTurnValueProperty().get() == 0) {
					switchTurn1Btn.setDisable(false);
					switchTurn2Btn.setDisable(true);
				}
			} else {
				switchTurn1Btn.setDisable(true);
				switchTurn2Btn.setDisable(true);
			}
		});
		// Add handlers for turn switching buttons
		switchTurn1Btn.setOnAction(handleSwitchTurnBtnClick);
		switchTurn2Btn.setOnAction(handleSwitchTurnBtnClick);
		
		// Handler for when mouse moves over scene
		scene.setOnMouseMoved(handleMouseMoveOnScene);

		// Pop Up Dialog Window for player change
		changePlayerAlert.setTitle("Vuoron vaihto.");
		changePlayerAlert.headerTextProperty().bind(Bindings.createStringBinding(() ->
				String.format("Kutsu pelaaja %s paikalle.", game.playerInTurnNameProperty().get()), game.playerInTurnNameProperty()));
		changePlayerAlert.setContentText("Anna vuoro toiselle pelaajalle.");
		changePlayerAlert.setOnCloseRequest(handleTurnSwitchDialogAction);
		//changePlayerAlert.showAndWait();
		
		turnInfoText.setText(String.format("Peli alkaa. Pelaajan %s vuoro ampua.", game.playerInTurnNameProperty().get()));
	}
	
	/**
	 * This is an EventHandler to handle the Switch Turn Button click.
	 * When the button is clicked, it requests the player to give the turn to another player.
	 * This is indicated with a pop up dialog.
	 * When the other player closes the dialog and the turn is given,
	 * the turnInfoText is updated to show the player's name in turn.
	 */

	private EventHandler<ActionEvent> handleSwitchTurnBtnClick = new EventHandler<ActionEvent>() {
		
		@Override
		public void handle(ActionEvent event) {
			// Request the player to give the turn to another player.
			game.awaitingProperty().set(true);
			game.requestTurnChangeProperty().set(false);
			turnInfoText.setText("Vuoron vaihto.");
			changePlayerAlert.showAndWait();
			
			// The turn has been given
			turnInfoText.setText(String.format("Pelaajan %s vuoro ampua.",game.playerInTurnNameProperty().get()));
		}
	};
	
	/**
	 * This event handler is for when the turn switch dialog is activated.
	 * The event handler releases the turn for the other player.
	 */
	
	private EventHandler<DialogEvent> handleTurnSwitchDialogAction = new EventHandler<DialogEvent>() {
		
		@Override
		public void handle(DialogEvent event) {
			// Switch turns
			boolean player1turn = gameboardGUI1.isMyTurnProperty().get();
			gameboardGUI1.isMyTurnProperty().set(gameboardGUI2.isMyTurnProperty().get());
			gameboardGUI2.isMyTurnProperty().set(player1turn);
			game.awaitingProperty().set(false);
			game.turnIsOverProperty().set(false);
		}
	};
	
	/**
	 * This defines an EventHandler that will be used to handle mouse move events on the scene.
	 * The event handler will check if the mouse is over either gameboardGUI1 or gameboardGUI2,
	 * and if so, it will set the OnMouseOverValue for the other gameboard to false.
	 * Otherwise, if outside of both gameboards, it will set the OnMouseOverValue for both gameboards to false.
	 */

	private EventHandler<MouseEvent> handleMouseMoveOnScene = new EventHandler<MouseEvent>() {
		
		@Override
		public void handle(MouseEvent event) {
			Bounds grid1Bounds = gameboardGUI1.localToScene(gameboardGUI1.getBoundsInLocal());
			Bounds grid2Bounds = gameboardGUI2.localToScene(gameboardGUI2.getBoundsInLocal());
	
			if (grid1Bounds.contains(event.getX(), event.getY())) {
				gameboardGUI2.setOnMouseOverValue(false);
			} else if (grid2Bounds.contains(event.getX(), event.getY())) {
				gameboardGUI1.setOnMouseOverValue(false);
			} else {
				gameboardGUI1.setOnMouseOverValue(false);
				gameboardGUI2.setOnMouseOverValue(false);
			}
		}
	};
	

    public void setSoundBox(Group soundBoxContainer) {
    	this.soundBoxContainer = soundBoxContainer;
    	mainStackPane.widthProperty().addListener((obj, oldVal,newVal) -> {
    		this.soundBoxContainer.setTranslateX(newVal.doubleValue() / 2 - this.soundBoxContainer.getBoundsInLocal().getMaxX() / 2 - 20);
    	});
    	this.soundBoxContainer.setTranslateY(20);
    	mainStackPane.getChildren().add(this.soundBoxContainer);
	}
}
