package fi.utu.tech.gui.javafx;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
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
	private Pane gameboardPane;

	@FXML
	private BorderPane gameScreenPane;

	@FXML
	private Text playerText;
	
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
	private Image backgroundImage;
	private ImageView currentlyMoved = null;
	private List<ImageView> addedShips = new ArrayList<>();;
	private double rotation = 0.0;
	private SimpleIntegerProperty battleshipAmountProperty = new SimpleIntegerProperty();
	private SimpleIntegerProperty carrierAmountProperty = new SimpleIntegerProperty();
	private SimpleIntegerProperty cruiserAmountProperty = new SimpleIntegerProperty(); 
	private SimpleIntegerProperty destroyerAmountProperty = new SimpleIntegerProperty();
	private SimpleIntegerProperty submarineAmountProperty = new SimpleIntegerProperty();
	private Alert alert = new Alert(AlertType.INFORMATION);

	@FXML
	public void initialize() {
		
		this.battleshipImage = new Image(getClass().getResource("ShipBattleshipHull2.png").toExternalForm());
		battleship.setImage(this.battleshipImage);

		this.carrierImage = new Image(getClass().getResource("ShipCarrierHull2.png").toExternalForm());
		carrier.setImage(carrierImage);
		
		this.cruiserImage = new Image(getClass().getResource("ShipCruiserHull2.png").toExternalForm());
		cruiser.setImage(cruiserImage);

		this.destroyerImage = new Image(getClass().getResource("ShipDestroyerHull2.png").toExternalForm());
		destroyer.setImage(destroyerImage);

		this.submarineImage = new Image(getClass().getResource("ShipSubmarineHull2.png").toExternalForm());
		submarine.setImage(submarineImage);
		
		this.backgroundImage = new Image(getClass().getResource("sea_texture.jpg").toExternalForm());
		gameboardPane.setBackground(new Background(new BackgroundImage(this.backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		
		StringConverter<Number> NumberConverter = new NumberStringConverter();
		Bindings.bindBidirectional(battleshipText.textProperty(), this.battleshipAmountProperty, NumberConverter);
		Bindings.bindBidirectional(carrierText.textProperty(), this.carrierAmountProperty, NumberConverter);
		Bindings.bindBidirectional(cruiserText.textProperty(), this.cruiserAmountProperty, NumberConverter);
		Bindings.bindBidirectional(destroyerText.textProperty(), this.destroyerAmountProperty, NumberConverter);
		Bindings.bindBidirectional(submarineText.textProperty(), this.submarineAmountProperty, NumberConverter);
		this.endPlacementButton.setDisable(true);
		
		this.alert.setTitle("Huomio!");
		
		playerText.textProperty().bind(Bindings.createStringBinding(() -> String.format("Pelaajan %s vuoro asettaa laivat",
				game.playerInTurnNameProperty().get()), game.playerInTurnNameProperty()));
	}
	
	// Method for initializing visual things
		public void drawBoard() {
			gameboardSize = this.game.boardSizeProperty().get();
			for (int i = 0; i < gameboardSize + 1; i++) {

				// Vertical lines
				Line verticaLine = new Line();
				verticaLine.startXProperty().bind(gameboardPane.widthProperty().divide(this.gameboardSize).multiply(i));
				verticaLine.endXProperty().bind(gameboardPane.widthProperty().divide(this.gameboardSize).multiply(i));
				verticaLine.endYProperty().bind(gameboardPane.heightProperty());

				// Horizontal lines
				Line horizontalLine = new Line();
				horizontalLine.startYProperty().bind(gameboardPane.heightProperty().divide(this.gameboardSize).multiply(i));
				horizontalLine.endYProperty().bind(gameboardPane.heightProperty().divide(this.gameboardSize).multiply(i));
				horizontalLine.endXProperty().bind(gameboardPane.widthProperty());

				gameboardPane.getChildren().add(verticaLine);
				gameboardPane.getChildren().add(horizontalLine);
				
			}
			this.battleshipAmountProperty.set(this.game.shipCountProperties()[1].get());
			this.carrierAmountProperty.set(this.game.shipCountProperties()[0].get());
			this.cruiserAmountProperty.set(this.game.shipCountProperties()[2].get());
			this.destroyerAmountProperty.set(this.game.shipCountProperties()[4].get());
			this.submarineAmountProperty.set(this.game.shipCountProperties()[3].get());
			
			endPlacementButton.disableProperty().bind(game.getBoard().readyProperty().not());
		}
	

	// Method for dragging a ship
	@FXML
	void dragMove(MouseEvent e) {
		String sourceText = e.getSource().toString();
		if (this.currentlyMoved == null) {

			if (sourceText.contains("battleship")) {
				if (this.battleshipAmountProperty.get() == 0) {
					return;
				}
				this.currentlyMoved = new ImageView(this.battleshipImage);
				this.currentlyMoved.setFitWidth(gameboardPane.getWidth() / this.gameboardSize * 4);
			} else if (sourceText.contains("carrier")) {
				if (this.carrierAmountProperty.get() == 0) {
					return;
				}
				this.currentlyMoved = new ImageView(this.carrierImage);
				this.currentlyMoved.setFitWidth(gameboardPane.getWidth() / this.gameboardSize * 5);
			} else if (sourceText.contains("cruiser")) {
				if (this.cruiserAmountProperty.get() == 0) {
					return;
				}
				this.currentlyMoved = new ImageView(this.cruiserImage);
				this.currentlyMoved.setFitWidth(gameboardPane.getWidth() / this.gameboardSize * 3);
			} else if (sourceText.contains("destroyer")) {
				if (this.destroyerAmountProperty.get() == 0) {
					return;
				}
				this.currentlyMoved = new ImageView(this.destroyerImage);
				this.currentlyMoved.setFitWidth(gameboardPane.getWidth() / this.gameboardSize * 2);
			} else if (sourceText.contains("submarine")) {
				if (this.submarineAmountProperty.get() == 0) {
					return;
				}
				this.currentlyMoved = new ImageView(this.submarineImage);
				this.currentlyMoved.setFitWidth(gameboardPane.getWidth() / this.gameboardSize * 3);
			}
			this.currentlyMoved.setMouseTransparent(true);
			this.currentlyMoved.setFitHeight(gameboardPane.getHeight() / this.gameboardSize);
			gameScreenPane.getChildren().add(currentlyMoved);
		}
		double offsetX = gameboardPane.getHeight() / this.gameboardSize / 2;
		double offsetY = gameboardPane.getWidth() / this.gameboardSize / 2;
		this.currentlyMoved.setLayoutX(e.getSceneX() - offsetX);
		this.currentlyMoved.setLayoutY(e.getSceneY() - offsetY);
	}


	// Method for removing lastly added ship
	@FXML
	void removeLatestShip() {
		int lastShipIndex = this.addedShips.size() - 1;
		if (lastShipIndex >= 0) {
			this.changeShipAmount(addedShips.get(lastShipIndex).getImage().getUrl().toLowerCase(), false);
			gameScreenPane.getChildren().remove(this.addedShips.get(lastShipIndex));
			this.addedShips.remove(lastShipIndex);
			game.getBoard().removeLastShip();
		} else {
			this.alert.setHeaderText("Pelilaudalle ei ole asetettu laivoja.");
			this.alert.show();
		}
	}

	// Method for removing all ships from gameboard
	@FXML
	void removeAllShips() {
		if (this.addedShips.size() == 0) {
			this.alert.setHeaderText("Pelilaudalle ei ole asetettu laivoja.");
			this.alert.show();
			return;
		}
		for (ImageView ship : this.addedShips) {
			this.changeShipAmount(ship.getImage().getUrl().toLowerCase(), false);
			gameScreenPane.getChildren().remove(ship);
		}
		this.addedShips.clear();
		this.game.removeAllShips();
	}

	// Method for "dropping" the ship on the gameboard
	@FXML
	void dragFinished(MouseEvent e) {
		// Mouse need to be inside the boardPane, if not, then abort the setting.
		if (!gameboardPane.getBoundsInParent().contains(e.getSceneX(), e.getSceneY())) {
			gameScreenPane.getChildren().remove(this.currentlyMoved);
			this.currentlyMoved = null;
			this.rotation = 0.0;
			this.alert.setHeaderText("Tähän ei voi asettaa laivaa.");
			this.alert.show();
			return;
		}
		if (e.getButton() == MouseButton.PRIMARY) {
			if (this.currentlyMoved != null) {
				String shipImageUrl = this.currentlyMoved.getImage().getUrl().toLowerCase();
				int xCoordinate = (int) Math.floor(e.getSceneX() / gameboardPane.getWidth() * this.gameboardSize);
				int yCoordinate = (int) Math.floor((e.getSceneY() - 100) / gameboardPane.getHeight() * this.gameboardSize);
				
				// Lock ship in the placed coordinate
				this.currentlyMoved.setLayoutX(xCoordinate * (gameboardPane.getWidth() / this.gameboardSize));
				this.currentlyMoved.setLayoutY(yCoordinate * (gameboardPane.getHeight() / this.gameboardSize) + 100);
				
				// Get the Orientation of the ship
				Orientation orientation = null;
				switch (this.getShipsRotationDirection()) {
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
				
				// Get the ShipType
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

				// Create XY from coordinates
				XY coords = new XY(xCoordinate, yCoordinate);
				// Create ship
				Ship ship = this.game.createShip(shipType, coords, orientation);
				// Add the ship to gameboard
				if (!this.game.addShip(ship)) {
					gameScreenPane.getChildren().remove(this.currentlyMoved);
					this.alert.setHeaderText("Tähän ei voi asettaa laivaa.");
					this.alert.show();
					this.currentlyMoved = null;
					this.rotation = 0.0;
					return;
				}
				
				// Set everything back to default
				this.rotation = 0.0;
				this.addedShips.add(currentlyMoved);
				this.changeShipAmount(shipImageUrl, true);
				this.currentlyMoved = null;
			}
		}

	}

	// Rotate ship while dragging it
	@FXML
	void rotateShip(KeyEvent e) {
		if (this.currentlyMoved != null) {
			Rotate rotate = new Rotate();

			if (e.getEventType() == KeyEvent.KEY_TYPED && e.getCharacter().equals(("r"))) {
				this.rotation += 90;
				if (this.rotation > 270) {
					this.rotation = 0;
				}

				double offsetX = gameboardPane.getHeight() / this.gameboardSize / 2;
				double offsetY = gameboardPane.getWidth() / this.gameboardSize / 2;
				rotate.setAngle(90.0);
				rotate.setPivotX(this.currentlyMoved.getX() + offsetX);
				rotate.setPivotY(this.currentlyMoved.getY() + offsetY);
				this.currentlyMoved.getTransforms().addAll(rotate);
			}
		}
	}

	
	// Change the available amount of the ships
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
		

	// Calculate the direction of ships rotation
	private int getShipsRotationDirection() {
		return (int) rotation / 90;
	}
		
	// Close game
	@FXML
	void endGame() {
		Platform.exit();
	}

	// Get endPlacementButton
	public Button getEndPlacementButton() {
		return this.endPlacementButton;
	}

}
