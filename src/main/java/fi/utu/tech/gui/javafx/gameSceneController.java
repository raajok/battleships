package fi.utu.tech.gui.javafx;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class gameSceneController {
	private BattleshipGame game = MainApp.getGame();
	private Group elements = new Group();
	private Group gridlines = new Group();
	private Group[] shots = {new Group(), new Group()};
	private int gameboardSize = 10;
	private Alert changePlayerAlert = new Alert(AlertType.INFORMATION);
	private Alert hitSuccessfulAlert = new Alert(AlertType.INFORMATION);
	
	@FXML
    private Button endGameButton;
	
	@FXML
	private Pane gameboardPane;
	
	@FXML
	private Pane gameSceneBorderPane;
	
	@FXML
	private Text playerInfoText;
	
	@FXML
	private void initialize() {
		// Binding for "player in turn -info"
		playerInfoText.textProperty().bind(Bindings.createStringBinding(() -> 
											String.format("Pelaajan '%s' vuoro ampua.",
													game.playerInTurnNameProperty().get()), 
											game.playerInTurnNameProperty()));
		
		game.newGameTest("name1", "name2", gameboardSize);
		
		// Pop Up Dialog Window for player change
		changePlayerAlert.setTitle("Toisen pelaajan vuoro.");
		changePlayerAlert.setHeaderText("Pelaajan vaihto.");
		changePlayerAlert.contentTextProperty().bind(playerInfoText.textProperty());
		
		// Pop Up Dialog Window for a successful hit
		hitSuccessfulAlert.setTitle("Osuma!");
		hitSuccessfulAlert.setHeaderText("Osuma!");
		hitSuccessfulAlert.setContentText("Ammuit onnistuneesti vihollisen laivaan.");

		// Render a grid
		for(int i = 0; i <= gameboardSize; i++) {
			Line verticaLine = new Line();
			verticaLine.startXProperty().bind(gameboardPane.widthProperty().divide(gameboardSize).multiply(i));
			verticaLine.endXProperty().bind(gameboardPane.widthProperty().divide(gameboardSize).multiply(i));
			verticaLine.endYProperty().bind(gameboardPane.heightProperty());
			Line horizontalLine = new Line();
			horizontalLine.startYProperty().bind(gameboardPane.heightProperty().divide(gameboardSize).multiply(i));
			horizontalLine.endYProperty().bind(gameboardPane.heightProperty().divide(gameboardSize).multiply(i));
			horizontalLine.endXProperty().bind(gameboardPane.widthProperty());
			gridlines.getChildren().add(verticaLine);
			gridlines.getChildren().add(horizontalLine);
		}
		
		shots[Player.PLAYER1.ordinal()].visibleProperty().bind(game.playerInTurnValueProperty().isNotEqualTo(Player.PLAYER1.ordinal()));
		shots[Player.PLAYER2.ordinal()].visibleProperty().bind(game.playerInTurnValueProperty().isNotEqualTo(Player.PLAYER2.ordinal()));
		
		gameSceneBorderPane.visibleProperty().bind(changePlayerAlert.showingProperty().not());
		
		elements.getChildren().add(gridlines);
		elements.getChildren().add(shots[0]);
		elements.getChildren().add(shots[1]);
		gameboardPane.getChildren().add(elements);
		changePlayerAlert.showAndWait();
	}
	
	@FXML
	public void handleGameboardClick(MouseEvent event) {
		// Convert the corresponding mouse coordinates to board target coordinates
		XY targetCoord = new XY((int) Math.floor(event.getX() / gameboardPane.getWidth() * 10/*gameboardSize*/),
				(int) Math.floor(event.getY() / gameboardPane.getHeight() * 10/*gameboardSize*/));
		
		// Shoot to target
		int result = game.shootTest(targetCoord);
		if (result == 0) {
			// Missed
			Circle circle = new Circle(0,0,10);
			circle.centerXProperty().bind(gameboardPane.widthProperty().divide(gameboardSize).multiply(targetCoord.getX()));
			circle.centerYProperty().bind(gameboardPane.heightProperty().divide(gameboardSize).multiply(targetCoord.getY()));
			circle.translateXProperty().bind(gameboardPane.widthProperty().divide(gameboardSize*2));
			circle.translateYProperty().bind(gameboardPane.heightProperty().divide(gameboardSize*2));
			shots[game.playerInTurnValueProperty().get()].getChildren().add(circle);	
		} else if (result == 1) {
			// Hit on target
			hitSuccessfulAlert.showAndWait();
			Circle circle = new Circle(0,0,10);
			circle.setFill(Color.RED);
			circle.centerXProperty().bind(gameboardPane.widthProperty().divide(gameboardSize).multiply(targetCoord.getX()));
			circle.centerYProperty().bind(gameboardPane.heightProperty().divide(gameboardSize).multiply(targetCoord.getY()));
			circle.translateXProperty().bind(gameboardPane.widthProperty().divide(gameboardSize*2));
			circle.translateYProperty().bind(gameboardPane.heightProperty().divide(gameboardSize*2));
			shots[game.playerInTurnValueProperty().get()].getChildren().add(circle);	
		}
		changePlayerAlert.showAndWait();
	};

	@FXML
    public void handleEndGameButtonClick(ActionEvent event) {		
	};

}
