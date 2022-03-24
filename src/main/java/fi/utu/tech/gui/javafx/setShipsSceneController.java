package fi.utu.tech.gui.javafx;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class setShipsSceneController {

	@FXML
	private Button resetShipsButton;

	@FXML
	private Button endGameButton;

	@FXML
	private Button endPlacementButton;

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
	
	@FXML
	private Text battleshipText;

	@FXML
	private Text carrierText;

	@FXML
	private Text cruiserText;

	@FXML
	private Text destroyerText;

	@FXML
	private Text submarineText;

	private BattleshipGame game = MainApp.getGame();
	private int gameboardSize;
	private Image battleshipImage;
	private Image carrierImage;
	private Image cruiserImage;
	private Image destroyerImage;
	private Image submarineImage;
	private ImageView currentlyMoved = null;
	private List<ImageView> addedShips = new ArrayList<>();;
	private double rotation = 0.0;
	private SimpleIntegerProperty battleshipAmountProperty = new SimpleIntegerProperty();
	private SimpleIntegerProperty carrierAmountProperty = new SimpleIntegerProperty();
	private SimpleIntegerProperty cruiserAmountProperty = new SimpleIntegerProperty(); 
	private SimpleIntegerProperty destroyerAmountProperty = new SimpleIntegerProperty();
	private SimpleIntegerProperty submarineAmountProperty = new SimpleIntegerProperty(); 

	@FXML
	public void initialize() {	
		
		this.battleshipImage = new Image(getClass().getResource("Battleship_placeholder.png").toExternalForm());
		battleship.setImage(this.battleshipImage);

		this.carrierImage = new Image(getClass().getResource("Carrier_placeholder.png").toExternalForm());
		carrier.setImage(carrierImage);

		this.cruiserImage = new Image(getClass().getResource("Cruiser_placeholder.png").toExternalForm());
		cruiser.setImage(cruiserImage);

		this.destroyerImage = new Image(getClass().getResource("Destroyer_placeholder.png").toExternalForm());
		destroyer.setImage(destroyerImage);

		this.submarineImage = new Image(getClass().getResource("Submarine_placeholder.png").toExternalForm());
		submarine.setImage(submarineImage);
		
		StringConverter<Number> NumberConverter = new NumberStringConverter();
		Bindings.bindBidirectional(battleshipText.textProperty(), this.battleshipAmountProperty, NumberConverter);
		Bindings.bindBidirectional(carrierText.textProperty(), this.carrierAmountProperty, NumberConverter);
		Bindings.bindBidirectional(cruiserText.textProperty(), this.cruiserAmountProperty, NumberConverter);
		Bindings.bindBidirectional(destroyerText.textProperty(), this.destroyerAmountProperty, NumberConverter);
		Bindings.bindBidirectional(submarineText.textProperty(), this.submarineAmountProperty, NumberConverter);
	}

	@FXML
	void resetShips() {
		// koko ruudun koko
		// System.out.println("outerpane width: " + outerPane.getWidth());
		// System.out.println("outerpane height: " + outerPane.getHeight());
		// System.out.println("topbox height: " + topBox.getHeight());
		// System.out.println("topbox height: " + topBox.getWidth());
		// System.out.println("bottombox height: " + bottomBox.getHeight());
		// System.out.println("bottombox height: " + bottomBox.getWidth());
		// peliruudukon panen koko
		// System.out.println("width:" + boardPane.getWidth());
		// System.out.println("height:" + boardPane.getHeight());
		// top h
		System.out.println(gameboardSize);
	};
	
	// Close game
	@FXML
	void endGame() {
		Platform.exit();
	}

	@FXML
	void cancelLastShipPlacement() {
		int lastShipIndex = this.addedShips.size() - 1;
		System.out.println(lastShipIndex);
		if (lastShipIndex >= 0) {
			changeShipAmount(addedShips.get(lastShipIndex).getImage().getUrl().toLowerCase(), false);
			outerPane.getChildren().remove(this.addedShips.get(lastShipIndex));
			this.addedShips.remove(lastShipIndex);
		}
	}

	@FXML
	void removeAllShips() {
		for (ImageView ship : this.addedShips) {
			changeShipAmount(ship.getImage().getUrl().toLowerCase(), false);
			outerPane.getChildren().remove(ship);
		}
		this.addedShips.clear();
	}

	@FXML
	void printCoords(MouseEvent e) {
		int rectangleX = (int) Math.floor(e.getX() / boardPane.getWidth() * this.gameboardSize);
		int rectangleY = (int) Math.floor(e.getY() / boardPane.getHeight() * this.gameboardSize);
	}

	@FXML
	void dragMove(MouseEvent e) {
		String sourceText = e.getSource().toString();
		if (this.currentlyMoved == null) {

			if (sourceText.contains("battleship")) {
				if (this.battleshipAmountProperty.get() == 0) {
					return;
				}
				this.currentlyMoved = new ImageView(this.battleshipImage);
				this.currentlyMoved.setFitWidth(boardPane.getWidth() / this.gameboardSize * 4);
			} else if (sourceText.contains("carrier")) {
				if (this.carrierAmountProperty.get() == 0) {
					return;
				}
				this.currentlyMoved = new ImageView(this.carrierImage);
				this.currentlyMoved.setFitWidth(boardPane.getWidth() / this.gameboardSize * 5);
			} else if (sourceText.contains("cruiser")) {
				if (this.cruiserAmountProperty.get() == 0) {
					return;
				}
				this.currentlyMoved = new ImageView(this.cruiserImage);
				this.currentlyMoved.setFitWidth(boardPane.getWidth() / this.gameboardSize * 3);
			} else if (sourceText.contains("destroyer")) {
				if (this.destroyerAmountProperty.get() == 0) {
					return;
				}
				this.currentlyMoved = new ImageView(this.destroyerImage);
				this.currentlyMoved.setFitWidth(boardPane.getWidth() / this.gameboardSize * 2);
			} else if (sourceText.contains("submarine")) {
				if (this.submarineAmountProperty.get() == 0) {
					return;
				}
				this.currentlyMoved = new ImageView(this.submarineImage);
				this.currentlyMoved.setFitWidth(boardPane.getWidth() / this.gameboardSize * 3);
			}
			this.currentlyMoved.setMouseTransparent(true);
			this.currentlyMoved.setFitHeight(boardPane.getHeight() / this.gameboardSize);
			outerPane.getChildren().add(currentlyMoved);
		}
		double offsetX = boardPane.getHeight() / this.gameboardSize / 2;
		double offsetY = boardPane.getWidth() / this.gameboardSize / 2;
		this.currentlyMoved.setLayoutX(e.getSceneX() - offsetX);
		this.currentlyMoved.setLayoutY(e.getSceneY() - offsetY);
		int rectangleX = (int) Math.floor((e.getSceneX() - 7) / boardPane.getWidth() * this.gameboardSize);
		int rectangleY = (int) Math.floor((e.getSceneY() - 100) / boardPane.getHeight() * this.gameboardSize);
	}
	
	
	@FXML
	void dragFinished(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY) {
			if (this.currentlyMoved != null) {
				String shipImageUrl = this.currentlyMoved.getImage().getUrl().toLowerCase();
				int xCoordinate = (int) Math.floor((e.getSceneX() - 7) / boardPane.getWidth() * this.gameboardSize);
				int yCoordinate = (int) Math.floor((e.getSceneY() - 100) / boardPane.getHeight() * this.gameboardSize);
				// System.out.println("ship rotation direction: " + getShipsRotationDirection());
				
				// get the Orientation of the ship
				Orientation orientation = null;
				switch (getShipsRotationDirection()) {
				case 0:
					orientation = Orientation.RIGHT;
					break;
				case 1:
					orientation = Orientation.DOWN;
					break;
				case 2: 
					orientation = Orientation.LEFT;
					break;
				case 3:
					orientation = Orientation.UP;
					break;
				}
				
				// get the ShipType
				ShipType shipType = null;
				if (shipImageUrl.contains("battleship")) {
					shipType = ShipType.BATTLESHIP;
				} else if (shipImageUrl.contains("carrier")) {
					shipType = ShipType.CARRIER;
				} else if (shipImageUrl.contains("cruiser")) {
					shipType = ShipType.CRUISER;
				} else if (shipImageUrl.contains("destroyer")) {
					shipType = ShipType.DESTROYER;
				} else if (shipImageUrl.contains("submarine")) {
					shipType = ShipType.SUBMARINE;
				}
				System.out.println(orientation);
				// create XY from coordinates
				XY coords = new XY(xCoordinate, yCoordinate);
				// create ship
				Ship ship = this.game.createShip(shipType, coords, orientation);
				// add the ship to gameboard
				this.game.addShip(ship);
				
				// Set everything back to default
				this.rotation = 0.0;
				this.addedShips.add(currentlyMoved);
				changeShipAmount(shipImageUrl, true);
				this.currentlyMoved = null;
			}
		}

	}

	// Rotate ship
	@FXML
	void rotateShip(KeyEvent e) {
		if (this.currentlyMoved != null) {
			Rotate rotate = new Rotate();

			if (e.getEventType() == KeyEvent.KEY_TYPED && e.getCharacter().equals(("r"))) {
				this.rotation += 90;
				if (this.rotation > 270) {
					this.rotation = 0;
				}

				System.out.println(this.rotation);
				double offsetX = boardPane.getHeight() / this.gameboardSize / 2;
				double offsetY = boardPane.getWidth() / this.gameboardSize / 2;
				rotate.setAngle(90.0);
				rotate.setPivotX(this.currentlyMoved.getX() + offsetX);
				rotate.setPivotY(this.currentlyMoved.getY() + offsetY);
				this.currentlyMoved.getTransforms().addAll(rotate);
			}
		}
	}

	// Draw gameboard
	public void drawBoard() {
		gameboardSize = this.game.boardSizeProperty().get();
		for (int i = 0; i < gameboardSize + 1; i++) {

			// Vertical lines
			Line verticaLine = new Line();
			verticaLine.startXProperty().bind(boardPane.widthProperty().divide(this.gameboardSize).multiply(i));
			verticaLine.endXProperty().bind(boardPane.widthProperty().divide(this.gameboardSize).multiply(i));
			verticaLine.endYProperty().bind(boardPane.heightProperty());

			// Horizontal lines
			Line horizontalLine = new Line();
			horizontalLine.startYProperty().bind(boardPane.heightProperty().divide(this.gameboardSize).multiply(i));
			horizontalLine.endYProperty().bind(boardPane.heightProperty().divide(this.gameboardSize).multiply(i));
			horizontalLine.endXProperty().bind(boardPane.widthProperty());

			boardPane.getChildren().add(verticaLine);
			boardPane.getChildren().add(horizontalLine);
			
		}
		this.battleshipAmountProperty.set(this.game.shipCountProperties()[1].get());
		this.carrierAmountProperty.set(this.game.shipCountProperties()[0].get());
		this.cruiserAmountProperty.set(this.game.shipCountProperties()[2].get());
		this.destroyerAmountProperty.set(this.game.shipCountProperties()[4].get());
		this.submarineAmountProperty.set(this.game.shipCountProperties()[3].get());
		
		playerText.setText("Pelaajan " + this.game.getPlayerNamesProperty()[this.game.getPlayerInTurn().ordinal()].getValue() 
				+ " vuoro asettaa laivat");
	}

	// Calculate direction of ships rotation
	private int getShipsRotationDirection() {
		return (int) rotation / 90;
	}
	
	private void changeShipAmount(String shipType, boolean remove) {
		if (remove) {
			if (shipType.contains("battleship")) {
				this.battleshipAmountProperty.set(this.battleshipAmountProperty.get() - 1);
			} else if (shipType.contains("carrier")) {
				this.carrierAmountProperty.set(this.carrierAmountProperty.get() - 1);
			} else if (shipType.contains("cruiser")) {
				this.cruiserAmountProperty.set(this.cruiserAmountProperty.get() - 1);
			} else if (shipType.contains("destroyer")) {
				this.destroyerAmountProperty.set(this.destroyerAmountProperty.get() - 1);
			} else if (shipType.contains("submarine")) {
				this.submarineAmountProperty.set(this.submarineAmountProperty.get() - 1);
			}
		} else {
			if (shipType.contains("battleship")) {
				this.battleshipAmountProperty.set(this.battleshipAmountProperty.get() + 1);
			} else if (shipType.contains("carrier")) {
				this.carrierAmountProperty.set(this.carrierAmountProperty.get() + 1);
			} else if (shipType.contains("cruiser")) {
				this.cruiserAmountProperty.set(this.cruiserAmountProperty.get() + 1);
			} else if (shipType.contains("destroyer")) {
				this.destroyerAmountProperty.set(this.destroyerAmountProperty.get() + 1);
			} else if (shipType.contains("submarine")) {
				this.submarineAmountProperty.set(this.submarineAmountProperty.get() + 1);
			}
		}
	}

	// Get endPlacementButton
	public Button getEndPlacementButton() {
		return this.endPlacementButton;
	}

}
