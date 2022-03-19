package fi.utu.tech.gui.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class gameSceneController {
	private BattleshipGame game = MainApp.getGame();
	private Group elements = new Group();
	private Group gridlines = new Group();
	private Group shots = new Group();
	private int gameboardSize = 10;
	
	@FXML
    private Button endGameButton;
	
	@FXML
	private Pane gameboardPane;
	
	@FXML
	private void initialize() {
		
		game.newGameTest("name1", "name2", gameboardSize);

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
		
		elements.getChildren().add(gridlines);
		elements.getChildren().add(shots);
		gameboardPane.getChildren().add(elements);
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
			shots.getChildren().add(circle);	
		} else if (result == 1) {
			// Hit on target
			Circle circle = new Circle(0,0,10);
			circle.setFill(Color.RED);
			circle.centerXProperty().bind(gameboardPane.widthProperty().divide(gameboardSize).multiply(targetCoord.getX()));
			circle.centerYProperty().bind(gameboardPane.heightProperty().divide(gameboardSize).multiply(targetCoord.getY()));
			circle.translateXProperty().bind(gameboardPane.widthProperty().divide(gameboardSize*2));
			circle.translateYProperty().bind(gameboardPane.heightProperty().divide(gameboardSize*2));
			shots.getChildren().add(circle);	
		}
	};

	@FXML
    public void handleEndGameButtonClick(ActionEvent event) {		
	};

}
