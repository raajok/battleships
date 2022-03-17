package fi.utu.tech.gui.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class setShipsSceneController {
	
	@FXML
	private Button resetShipsButton;
	
	@FXML
	private Button endGameButton;
	
	@FXML
	private Pane boardPane;
	
	@FXML
	private BorderPane outerPane;
	
	@FXML
	private Text playerText;
	
	private BattleshipGame game;
	
	@FXML
	public void initialize() {
		this.game = new BattleshipGame();
		
		for(int i = 0; i < 11/*gameboardSize + 1*/; i++) {
			System.out.println(boardPane.getMaxWidth());
			Line verticaLine = new Line();
			verticaLine.startXProperty().bind(boardPane.widthProperty().divide(10/*gameboardSize*/).multiply(i));
			verticaLine.endXProperty().bind(boardPane.widthProperty().divide(10/*gameboardSize*/).multiply(i));
			verticaLine.endYProperty().bind(boardPane.heightProperty());
			Line horizontalLine = new Line();
			horizontalLine.startYProperty().bind(boardPane.heightProperty().divide(10/*gameboardSize*/).multiply(i));
			horizontalLine.endYProperty().bind(boardPane.heightProperty().divide(10/*gameboardSize*/).multiply(i));
			horizontalLine.endXProperty().bind(boardPane.widthProperty());
			boardPane.getChildren().add(verticaLine);
			boardPane.getChildren().add(horizontalLine);
		}
	}
	
	@FXML
	void resetShips() {
		// koko ruudun koko
		System.out.println("outerpane width: " + outerPane.getWidth()); 
		System.out.println("outerpane height: " + outerPane.getHeight());
		//peliruudukon panen koko
		System.out.println("width:" + boardPane.getWidth());
		System.out.println("height:" + boardPane.getHeight());
	};
	
	@FXML
	void endGame() {
		
	}
	
	@FXML
	void printCoords(MouseEvent e) {
		int rectangleX = (int) Math.floor(e.getX() / boardPane.getWidth() * 10/*gameboardSize*/);
		int rectangleY = (int) Math.floor(e.getY() / boardPane.getHeight() * 10/*gameboardSize*/);
		playerText.setText(/*"x:" + e.getX() + " y:" + e.getY() + */" ruutu: (" + rectangleX + " : " + rectangleY + ")");
	}

}
