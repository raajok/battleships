package fi.utu.tech.gui.javafx;

import java.util.ArrayDeque;
import java.util.Collection;

import javafx.application.Platform;
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
import javafx.scene.shape.Line;
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
 		foreground.getChildren().addAll(hoveringSquare);
 		//elements.getChildren().addAll(shipsGroup, shotsGroup, foreground);
 		getChildren().addAll(shipsGroup, shotsGroup, foreground, rect);
 		
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
    	shipsGroup.getChildren().addAll(loadShipImages(ships));
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
			
			shipImage.translateXProperty().bind(tileSize.multiply(ship.getLocation().getX()));
			shipImage.translateYProperty().bind(tileSize.multiply(ship.getLocation().getY()));
			shipImage.fitWidthProperty().bind(tileSize.subtract(1));
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
		Thread runAfterAnimationThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Platform.runLater(() -> {
					double lineWidth = tileSize.get() / 30;
					Color color = Color.BLACK.deriveColor(1, 1, .75, 1);
					
					// Line 1 from top left to bottom right
					Line line1 = new Line(lineWidth,lineWidth,tileSize.get()-lineWidth,tileSize.get()-lineWidth);
					line1.setStrokeWidth(lineWidth);
					line1.setStroke(color);
					line1.setTranslateX(tileSize.multiply(coord.getX()).get());
					line1.setTranslateY(tileSize.multiply(coord.getY()).get());
					
					// Line 2 from bottom left to top right
					Line line2 = new Line(lineWidth,tileSize.get()-lineWidth,tileSize.get()-lineWidth,lineWidth);
					line2.setStrokeWidth(lineWidth);
					line2.setStroke(color);
					line2.setTranslateX(tileSize.multiply(coord.getX()).get());
					line2.setTranslateY(tileSize.multiply(coord.getY()).get());
					
					// Add lines
					shotsGroup.getChildren().addAll(line1, line2);					
				});
				
			}
		});
		
		ImageView explosionImageView = new ImageView();
		Sprite explosionAnimation = new Sprite(explosionImageView,
										new Image(ResourceLoader.image("splash_cropped.png"), 480,360,false,false),
										8, // Columns
										6, // Rows
										60, // Frame width
										60, // Frame height
										60, // FPS
										1); // Repeats

		explosionImageView.setPreserveRatio(true);
		explosionImageView.fitWidthProperty().bind(tileSize);
		explosionImageView.translateXProperty().bind(tileSize
				.multiply(coord.getX()));
		explosionImageView.translateYProperty().bind(tileSize
				.multiply(coord.getY()));
		shotsGroup.getChildren().add(explosionImageView);
		runAfterAnimationThread.setDaemon(true);
		explosionAnimation.setRunAfter(runAfterAnimationThread);
		explosionAnimation.start();
	}
	
	private void addHitMarkTo(XY coord) {
		Image img = new Image(ResourceLoader.image("crater2.png"));
		
		ImageView crater = new ImageView(img);
		crater.setPreserveRatio(true);
		crater.fitWidthProperty().bind(tileSize.multiply(0.8));
		crater.translateXProperty().bind(tileSize
				.multiply(coord.getX()).add(tileSize.divide(10).get()));
		crater.translateYProperty().bind(tileSize
				.multiply(coord.getY()).add(tileSize.divide(10).get())
				);
		crater.setMouseTransparent(true);
		shotsGroup.getChildren().add(crater);

		ImageView explosionImageView = new ImageView();
		Sprite explosionAnimation = new Sprite(explosionImageView,
										new Image(ResourceLoader.image("explosion_cropped_2.png"), 480,360,false,false),
										8, // Columns
										6, // Rows
										60, // Frame width
										60, // Frame height
										60, // FPS
										1); // Repeats

		explosionImageView.setPreserveRatio(true);
		explosionImageView.fitWidthProperty().bind(tileSize);
		explosionImageView.translateXProperty().bind(tileSize
				.multiply(coord.getX()));
		explosionImageView.translateYProperty().bind(tileSize
				.multiply(coord.getY()));
		shotsGroup.getChildren().add(explosionImageView);
		explosionAnimation.start();
	}
	
	public StringProperty infoTextProperty() {
		return infoText;
	}
	
	// Event handlers
	
	private EventHandler<MouseEvent> onMouseMoveHandler = new EventHandler<MouseEvent>() {
		
		@Override
		public void handle(MouseEvent event) {
			onMouseOver.set(true);
			// Calculate the coordinates
			int x = (int) Math.floor(event.getX() / tileSize.get());
			int y = (int) Math.floor(event.getY() / tileSize.get());
			XY coord = new XY(x,y);
			if (game.isShootable(coord)) {
				hoveringSquare.setFill(transparentGreen);
			} else {
				hoveringSquare.setFill(transparentRed);
			}
			hoveringSquare.setTranslateX(coord.getX() * tileSize.get());
			hoveringSquare.setTranslateY(coord.getY() * tileSize.get());
		};
	};
	
	private EventHandler<MouseEvent> onMouseClickedHandler = new EventHandler<MouseEvent>() {
		
		@Override
		public void handle(MouseEvent event) {
			// If this is enemy board, then shoot.
			if (isMyTurn.not().and(game.requestTurnChangeProperty().not()).get()) {
				// Calculate the coordinates
				int x = (int) Math.floor(event.getX() / tileSize.get());
				int y = (int) Math.floor(event.getY() / tileSize.get());
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
								infoText.set("Osuit! Saat jatkaa.");
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
