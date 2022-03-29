package fi.utu.tech.gui.javafx;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public class gameSceneController {
	private BattleshipGame game = MainApp.getGame();
	private int gameboardSize;
	private Alert changePlayerAlert = new Alert(AlertType.INFORMATION);
	private GameboardGUIComponent gameboardGUI1;
	private GameboardGUIComponent gameboardGUI2;
	
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
	private void initialize() {}
	
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
	
	public void init(Scene scene) {
		
		// Note: the game.newGame() method has to be called before this is executed. It should be called before leaving the setShipsScene.

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
}
