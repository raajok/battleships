package fi.utu.tech.gui.javafx;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
	
	@FXML
	private HBox topBox;
	
	@FXML
	private HBox bottomBox;
	
	@FXML
	private ImageView battleship;
	
	@FXML
	private ImageView carrier;
	
	@FXML
	private ImageView cruiser;
	
	@FXML
	private ImageView destroyer;
	
	@FXML
	private ImageView submarine;
	
	private BattleshipGame game = MainApp.getGame();
	private int gameboardSize;
	
	@FXML
	public void initialize() {
		Image battleshipImage = new Image(getClass().getResource("Battleship_placeholder.png").toExternalForm());
		battleship.setImage(battleshipImage);
		
		Image carrierImage = new Image(getClass().getResource("Carrier_placeholder.png").toExternalForm());
		carrier.setImage(carrierImage);
		
		Image cruiserImage = new Image(getClass().getResource("Cruiser_placeholder.png").toExternalForm());
		cruiser.setImage(cruiserImage);
		
		Image destroyerImage = new Image(getClass().getResource("Destroyer_placeholder.png").toExternalForm());
		destroyer.setImage(destroyerImage);
		
		Image submarineImage = new Image(getClass().getResource("Submarine_placeholder.png").toExternalForm());
		submarine.setImage(submarineImage);
	}
	
	@FXML
	void resetShips() {
		// koko ruudun koko
		//System.out.println("outerpane width: " + outerPane.getWidth()); 
		//System.out.println("outerpane height: " + outerPane.getHeight());
		//System.out.println("topbox height: " + topBox.getHeight());
		//System.out.println("topbox height: " + topBox.getWidth());
		//System.out.println("bottombox height: " + bottomBox.getHeight());
		//System.out.println("bottombox height: " + bottomBox.getWidth());
		//peliruudukon panen koko
		//System.out.println("width:" + boardPane.getWidth());
		//System.out.println("height:" + boardPane.getHeight());
		//top h
		System.out.println(gameboardSize);
	};
	
	@FXML
	void endGame() {
		
	}
	
	@FXML
	void printCoords(MouseEvent e) {
		int rectangleX = (int) Math.floor(e.getX() / boardPane.getWidth() * this.gameboardSize);
		int rectangleY = (int) Math.floor(e.getY() / boardPane.getHeight() * this.gameboardSize);
		playerText.setText(/*"x:" + e.getX() + " y:" + e.getY() + */" ruutu: (" + rectangleX + " : " + rectangleY + ")");
	}
	
	@FXML
	void moveShip() {
		
	}
	
	EventHandler<MouseEvent> moveship = new EventHandler<MouseEvent>() {
		
		@Override
		public void handle(MouseEvent e) {
			
		}		
	};
	
	public void drawBoard() {
		gameboardSize = game.boardSizeProperty().get();
		for(int i = 0; i < gameboardSize + 1; i++) {
			System.out.println(boardPane.getMaxWidth());
			Line verticaLine = new Line();
			verticaLine.startXProperty().bind(boardPane.widthProperty().divide(this.gameboardSize).multiply(i));
			verticaLine.endXProperty().bind(boardPane.widthProperty().divide(this.gameboardSize).multiply(i));
			verticaLine.endYProperty().bind(boardPane.heightProperty());
			Line horizontalLine = new Line();
			horizontalLine.startYProperty().bind(boardPane.heightProperty().divide(this.gameboardSize).multiply(i));
			horizontalLine.endYProperty().bind(boardPane.heightProperty().divide(this.gameboardSize).multiply(i));
			horizontalLine.endXProperty().bind(boardPane.widthProperty());
			boardPane.getChildren().add(verticaLine);
			boardPane.getChildren().add(horizontalLine);								
		}
	}

}
