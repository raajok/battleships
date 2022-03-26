package fi.utu.tech.gui.javafx;

import java.util.ArrayDeque;
import java.util.Collection;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class GameboardGUIComponent extends Pane {

	private BattleshipGame game = MainApp.getGame();
    private DoubleProperty scalingFactor = new SimpleDoubleProperty(1.0);
    private DoubleProperty tileSize = new SimpleDoubleProperty();
    private int gridSize;
	private Group shotsGroup = new Group();
	private Group shipsGroup = new Group();
	private Group elements = new Group();
	private Group foreground = new Group();
	private ObjectProperty<Bounds> windowBounds = new SimpleObjectProperty<Bounds>();
	private Canvas grid;
	private Color transparentRed;
	private Color transparentGreen;
	private Rectangle hoveringSquare;
	private BooleanProperty onMouseOver = new SimpleBooleanProperty(false);
	private BooleanProperty isMyTurn = new SimpleBooleanProperty(false);
	private StringProperty infoText = new SimpleStringProperty();

    public GameboardGUIComponent(int gridSize, Player player) {
    	
    	// Give 1. turn to Player1
    	isMyTurn.set(player.equals(Player.PLAYER1));

    	this.gridSize = gridSize;
        setPrefSize(600, 600);
        grid = new Canvas(600, 600);
        setStyle("-fx-background-color: lightgrey; -fx-border-color: black;");
        
        // 45% transparent colors for hovering squares
        transparentRed = new Color(1, 0, 00, .45);
        transparentGreen = new Color(0, .8, 00, .45);
        
        // Create a hovering transparent red square as a default color
        hoveringSquare = createTransparentSquare(transparentRed);

        // add scale transform
        scaleXProperty().bind(scalingFactor);
        scaleYProperty().bind(scalingFactor);
        
        // Bindings
        windowBounds.bind(this.boundsInLocalProperty());
        windowBounds.addListener((obg, oldVal, newVal) -> {
        	tileSize.setValue(windowBounds.get().getWidth() * scalingFactor.doubleValue() / gridSize);
        });
        shipsGroup.visibleProperty().bind(isMyTurn.and(game.awaitingProperty().not()));
        shotsGroup.visibleProperty().bind(game.awaitingProperty().not());
        hoveringSquare.visibleProperty().bind(onMouseOver.and(isMyTurn.not()).and(game.requestTurnChangeProperty().not()));
        
 		// Create a rectangle for the grid to display
        Rectangle rect = new Rectangle(0, 0, 600, 600);
        rect.setFill(Color.TRANSPARENT);
        
 		// Add all groups
 		foreground.getChildren().addAll(hoveringSquare, rect);
 		elements.getChildren().addAll(shipsGroup, shotsGroup, foreground);
 		getChildren().add(elements);
 		
 		// Set event handlers
 		this.setOnMouseMoved(onMouseMoveHandler);
 		this.setOnMouseClicked(onMouseClickedHandler);
 		
 		// Draw a grid over gameboards
 		addGrid();
    }

    /**
     * Add a grid to the canvas, send it to back
     */
    private void addGrid() {
    	
    	final double w = windowBounds.get().getWidth();
    	final double h = windowBounds.get().getHeight();

        // don't catch mouse events
        grid.setMouseTransparent(true);

        GraphicsContext gc = grid.getGraphicsContext2D();
        
        // Add background image
        gc.drawImage(new Image(ResourceLoader.image("sea_texture.jpg")), 0,0);

        // add grid
        gc.setStroke(Color.BLACK.deriveColor(1, 1, 1, 0.25));
        gc.setLineWidth(1.8);

        // draw grid lines
        double offset = w/gridSize;
        for( double i=offset; i < w; i+=offset) {
            // vertical
            gc.strokeLine( i, 0, i, h);
            // horizontal
            gc.strokeLine( 0, i, w, i);
        }
        
        
        getChildren().add(grid);

        grid.toBack();
    }

    public double getScale() {
        return scalingFactor.get();
    }
    
    public DoubleProperty scaleProperty() {
		return scalingFactor;
	}
    
    public void setScale( double scale) {
        scalingFactor.set(scale);
    }
    
    public void createShipImages(Collection<Ship> ships) {
    	for (ImageView img: loadShipImages(ships)) {
    		shipsGroup.getChildren().add(img);
    	}
    }
    
    // Method for loading ship images
 	private Collection<ImageView> loadShipImages(Collection<Ship> ships) {
 		Collection<ImageView> shipImages = new ArrayDeque<ImageView>();
 		for (Ship ship: ships) {
 			Image img = new Image(getShipImagePath(ship.getType()));
 			ImageView shipImage = new ImageView(img);
 			
 			Rotate rotate = new Rotate(ship.getOrientation().getDegrees());
 			rotate.pivotXProperty().bind(tileSize.divide(2));
			rotate.pivotYProperty().bind(tileSize.divide(2));
			shipImage.getTransforms().add(rotate);
			shipImage.setPreserveRatio(true);
			shipImage.fitWidthProperty().bind(tileSize);
			shipImage.translateXProperty().bind(tileSize
					.multiply(ship.getLocation().getX()));
			shipImage.translateYProperty().bind(tileSize
					.multiply(ship.getLocation().getY())
					);
 			shipImage.setMouseTransparent(true);
 			shipImages.add(shipImage);
 		}
 		return shipImages;
 	}
 	
 	// Helper method for getting ship image paths
 	private String getShipImagePath(ShipType shipType) {
 		switch (shipType) {
 			case CARRIER: return ResourceLoader.image("ShipCarrierHull.png");
 			case BATTLESHIP: return ResourceLoader.image("ShipBattleshipHull.png");
 			case CRUISER: return ResourceLoader.image("ShipCruiserHull.png");
 			case SUBMARINE: return ResourceLoader.image("ShipSubmarineHull.png");
 			case DESTROYER: return ResourceLoader.image("ShipDestroyerHull.png");
 			default: return null;
 		}
 	}
 	
	private Rectangle createTransparentSquare(Color color) {
		Rectangle square = new Rectangle(tileSize.get(), tileSize.get());
		square.setFill(color);
		square.widthProperty().bind(tileSize);
		square.heightProperty().bind(tileSize);
		square.setMouseTransparent(true);
		square.setVisible(false); // By default the square is not visible
		return square;
	}
	
	public ObjectProperty<Bounds> getWindowBounds() {
		return windowBounds;
	}
	
	public void setIsMyTurnValue(Boolean isMyTurn) {
		this.isMyTurn.setValue(isMyTurn);
	}
	
	public void setOnMouseOverValue(Boolean onMouseOver) {
		this.onMouseOver.setValue(onMouseOver);
	}
	
	public BooleanProperty isMyTurnProperty() {
		return isMyTurn;
	}
	
	private void addMissedMarkTo(XY coord) {
		Circle circle = new Circle(0,0,20);
		circle.setTranslateX(tileSize.multiply(coord.getX()).get());
		circle.setTranslateY(tileSize.multiply(coord.getY()).get());
		circle.setCenterX(tileSize.divide(2).get());
		circle.setCenterY(tileSize.divide(2).get());
		
		shotsGroup.getChildren().add(circle);
	}
	
	private void addHitMarkTo(XY coord) {
		Cross cross = new Cross(tileSize.get(), tileSize.get(), Color.RED);
		cross.setTranslateX(tileSize.multiply(coord.getX()).get());
		cross.setTranslateY(tileSize.multiply(coord.getY()).get());
		
		shotsGroup.getChildren().add(cross);
	}
	
	public StringProperty infoTextProperty() {
		return infoText;
	}
	
	// Event handlers
	
	private EventHandler<MouseEvent> onMouseMoveHandler = new EventHandler<MouseEvent>() {
		
		@Override
		public void handle(MouseEvent event) {
			onMouseOver.set(true);
			int x = (int) Math.round(event.getX() / tileSize.get() - .5);
			int y = (int) Math.round(event.getY() / tileSize.get() - .5);
			XY coord = new XY(x,y);
			
			if (game.isShootable(coord)) {
				hoveringSquare.setFill(transparentGreen);
			} else {
				hoveringSquare.setFill(transparentRed);						
			}
			
			hoveringSquare.setTranslateX(x * tileSize.get());
			hoveringSquare.setTranslateY(y * tileSize.get());
		};
	};
	
	private EventHandler<MouseEvent> onMouseClickedHandler = new EventHandler<MouseEvent>() {
		
		@Override
		public void handle(MouseEvent event) {
			// If this is enemy board, then shoot.
			if (isMyTurn.not().and(game.requestTurnChangeProperty().not()).get()) {
				// Calculate the coordinates
				int x = (int) Math.round(event.getX() / tileSize.get() - .5);
				int y = (int) Math.round(event.getY() / tileSize.get() - .5);
				XY coord = new XY(x,y);
				
				if (game.isShootable(coord)) {
					// This coordinate is shootable.
					// Shoot
					int result = game.shoot(coord);
					
					if (result == 0) {
						// Missed
						infoText.set("Ammuit ohi. Anna vuoro toiselle.");
						addMissedMarkTo(coord);
					} else if (result == 1) {
						// Hit
						Ship ship = game.getShipFrom(game.getOpponent(), coord);
						if (ship != null) {
							// Query for if ship has sunk
							if (ship.hasSunk()) {
								infoText.set(String.format("Vastustajan %s upposi", ship.getType()));								
							} else {
								infoText.set("Osuit! Pelaaja saa jatkaa.");
							}
						} else {
							System.out.println("NullPointerException: Ship is null");
						}
						addHitMarkTo(coord);
					}
				}
			}
		};
	};
}
