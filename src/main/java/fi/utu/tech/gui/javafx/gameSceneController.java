package fi.utu.tech.gui.javafx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class gameSceneController {
	private BattleshipGame game = MainApp.getGame();
	private Group[] elements = {new Group(), new Group()};
	private Group[] foreground = {new Group(), new Group()};
	private Group[] shots = {new Group(), new Group()};
	private int gameboardSize = 10;
	private Alert changePlayerAlert = new Alert(AlertType.INFORMATION);
	private ImageView seaImageView1;
	private ImageView seaImageView2;
	private ImageView explosionImageView1;
	private ImageView splashImageView1;
	private ImageView explosionImageView2;
	private ImageView splashImageView2;
	private Sprite explosionAnimation1;
	private Sprite splashAnimation1;
	private Sprite explosionAnimation2;
	private Sprite splashAnimation2;
	private GridPane[] grids = {new GridPane(), new GridPane()};
	private SimpleDoubleProperty squareSize = new SimpleDoubleProperty(60.0);
	private Map<Integer, XY> squareCoords1 = new HashMap<Integer, XY>();
	private Map<Integer, XY> squareCoords2 = new HashMap<Integer, XY>();
	private Rectangle hoveringSquare1;
	private Rectangle hoveringSquare2;
	private Color transparentRed;
	private Color transparentGreen;
	private SimpleBooleanProperty turnIsOver = new SimpleBooleanProperty(false);
	
	public gameSceneController() {
		Double alpha = .45; // 50% transparent
		transparentRed = new Color(1, 0, 00, alpha);
		transparentGreen = new Color(0, .8, 00, alpha);
		// Panel1 animations
		explosionImageView1 = new ImageView();
		explosionAnimation1 = new Sprite(explosionImageView1,
										new Image(ResourceLoader.image("explosion_cropped_2.png"), 480,360,false,false),
										8, // Columns
										6, // Rows
										60, // Frame width
										60, // Frame height
										60, // FPS
										1); // Repeats
		splashImageView1 = new ImageView();
		splashAnimation1 = new Sprite(splashImageView1,
										new Image(ResourceLoader.image("water_splash.png"), 400,160,false,false),
										5, // Columns
										2, // Rows
										80, // Frame width
										80, // Frame height
										40, // FPS
										1); // Repeats

		// Panel2 animations
		explosionImageView2 = new ImageView();
		explosionAnimation2 = new Sprite(explosionImageView2,
										new Image(ResourceLoader.image("explosion_cropped_2.png"), 640,480,false,false),
										8, // Columns
										6, // Rows
										80, // Frame width
										80, // Frame height
										60, // FPS
										1); // Repeats
		splashImageView2 = new ImageView();
		splashAnimation2 = new Sprite(splashImageView2,
										new Image(ResourceLoader.image("water_splash.png"), 400,160,false,false),
										5, // Columns
										2, // Rows
										80, // Frame width
										80, // Frame height
										40, // FPS
										1); // Repeats
	}
	
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
	private void initialize() {
		
	}
	
	public void init(Scene scene) {
		turnInfoText.setText(String.format("Peli alkaa. Pelaajan %s vuoro ampua.",game.playerInTurnNameProperty().get()));
		
		// Binding of squareSize to scene size
		squareSize.bind(Bindings.min(scene.widthProperty().divide(2).subtract(gameboardSize*5),
				scene.heightProperty()
				.subtract(headerBox.heightProperty())
				.subtract(gameboardSize*5)
				).divide(gameboardSize));
		
		// Bindings for Turn Switching buttons
		turnIsOver.addListener((obj, oldVal, newVal) -> {
			if (newVal == true) {
				if (game.playerInTurnValueProperty().get() == 0) {
					switchTurn1Btn.setDisable(true);
					switchTurn2Btn.setDisable(false);
				} else if (game.playerInTurnValueProperty().get() == 1) {
					switchTurn1Btn.setDisable(false);
					switchTurn2Btn.setDisable(true);
				}
			} else {
				System.out.println("works");
				switchTurn1Btn.setDisable(true);
				switchTurn2Btn.setDisable(true);
			}
		});
		
		// Create a hovering transparent red square as a default color
		hoveringSquare1 = createTransparentSquare(transparentRed);
		hoveringSquare2 = createTransparentSquare(transparentRed);
		foreground[0].getChildren().add(hoveringSquare1);
		foreground[1].getChildren().add(hoveringSquare2);
		
		// Create a test game
		game.newGameTest("name1", "name2", gameboardSize);
		
		// Pop Up Dialog Window for player change
		changePlayerAlert.setTitle("Vuoron vaihto.");
		changePlayerAlert.headerTextProperty().bind(Bindings.createStringBinding(() ->
				String.format("Kutsu pelaaja %s paikalle.", game.playerInTurnNameProperty().get()), game.playerInTurnNameProperty()));
		changePlayerAlert.setContentText("Anna vuoro toiselle pelaajalle.");;
		
		// Render the grids
		renderGrid(grids[0], gridStack1, squareCoords1);
		renderGrid(grids[1], gridStack2, squareCoords2);
		
		// Panel1 elements
		elements[0].getChildren().addAll(grids[0],
										 shots[1],
										 explosionImageView1,
										 splashImageView1,
										 foreground[0]);
		gridStack1.getChildren().add(elements[0]);
		
		// Panel2 elements
		elements[1].getChildren().addAll(grids[1],
										 shots[0],
										 explosionImageView2,
										 splashImageView2,
										 foreground[1]);
		gridStack2.getChildren().add(elements[1]);
		
		scene.setOnMouseMoved((event) -> {
			Bounds grid1Bounds = grids[0].localToScene(grids[0].getBoundsInLocal());
			Bounds grid2Bounds = grids[1].localToScene(grids[1].getBoundsInLocal());

			if (grid1Bounds.contains(event.getX(), event.getY())) {
				hoveringSquare2.setVisible(false);				
				return;
			} else if (grid2Bounds.contains(event.getX(), event.getY())) {
				hoveringSquare1.setVisible(false);	 
				return;
			} else {
				hoveringSquare1.setVisible(false);
				hoveringSquare2.setVisible(false);
			}
		});
	}
	
	private Rectangle createTransparentSquare(Color color) {
		Rectangle square = new Rectangle(squareSize.get(), squareSize.get());
		square.setFill(color);
		square.widthProperty().bind(squareSize);
		square.heightProperty().bind(squareSize);
		square.setMouseTransparent(true);
		square.setVisible(false); // By default the square is not visible
		return square;
	}
	
	
	private void renderGrid(GridPane grid, StackPane pane, Map<Integer, XY> squareCoords) {
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-image: url(\"" + ResourceLoader.image("sea_texture.jpg") + "\")");
        grid.getStyleClass().add("styled-grid");
        pane.translateYProperty().bind(pane.getScene()
        								.heightProperty()
        								.subtract(headerBox.heightProperty())
        								.subtract(grid.heightProperty())
        								.divide(2));
		final int prefSize = 600;
		for(int i=0; i<gameboardSize*gameboardSize; i++) {
			// Create a rectangle with desired properties
			Rectangle square = new Rectangle(prefSize/gameboardSize,prefSize/gameboardSize);
			square.widthProperty().bind(squareSize);													 
			square.heightProperty().bind(squareSize);
			square.setFill(Color.TRANSPARENT);
			squareCoords.put(square.hashCode(), new XY((i) % gameboardSize, Math.floorDiv(i, gameboardSize)));
			GridPane.setHgrow(square, Priority.ALWAYS);
			GridPane.setVgrow(square, Priority.ALWAYS);
			
			square.setOnMouseClicked((event) -> {
				/*
				 * This function is called when the user clicks on a square of the gameboard.
				 * It converts the mouse coordinates to board target coordinates, shoots to that target if possible, and updates 
				 * the GUI accordingly.
				 */
				
				Rectangle target = (Rectangle) event.getSource();
				XY coord = squareCoords.get(target.hashCode());
				//grids[0].getCellBounds(0, 0)
				//System.out.println(squareBounds.getMinX());
				int playerNum = game.playerInTurnValueProperty().get();
				if ((squareCoords1.containsKey(target.hashCode()) && playerNum == 0) || 
						(squareCoords2.containsKey(target.hashCode()) && playerNum == 1)) {
					// Return. Player has clicked on his own grid
					return;
				}
				
				if (game.isShootable(coord)) {
					// This coordinate is shootable.
					
					// Shoot
					int result = game.shootTest(coord);
					int boardNumber = game.getOpponent().ordinal();
					
					Bounds squareBounds = grids[boardNumber].getCellBounds(coord.getX(), coord.getY());
					if (result == 0) {
						// Missed
						turnInfoText.setText("Ammuit ohi. Anna vuoro toiselle.");
						Circle circle = new Circle(0,0,10);
						circle.translateXProperty().bind(squareSize.add(0.25).multiply(coord.getX()));
						circle.translateYProperty().bind(squareSize.add(0.25).multiply(coord.getY()));
						circle.scaleXProperty().bind(squareSize.divide(30));
						circle.scaleYProperty().bind(squareSize.divide(30));
						circle.centerXProperty().bind(squareSize.divide(2));
						circle.centerYProperty().bind(squareSize.divide(2));
						shots[boardNumber].getChildren().add(circle);
						/*
						if (boardNumber == 1) {
							splashImageView1.setTranslateX(squareBounds.getMinX());
							splashImageView1.setTranslateY(squareBounds.getMinY());
							splashAnimation1.start();								
						} else if (boardNumber == 0) {
							splashImageView2.setTranslateX(squareBounds.getMinX());
							splashImageView2.setTranslateY(squareBounds.getMinY());
							splashAnimation2.start();
						}
						*/
					} else if (result == 1) {
						// Hit on target
						turnInfoText.setText("Osuit! Anna vuoro toiselle.");
						//explosionImageView.setTranslateX(gameboardPane.getWidth() / gameboardSize * targetCoord.getX());
						//explosionImageView.setTranslateY(gameboardPane.getHeight() / gameboardSize * targetCoord.getY());
						//explosionAnimation.start();
						Circle circle = new Circle(0,0,10);
						circle.setFill(Color.RED);
						circle.translateXProperty().bind(squareSize.add(0.25).multiply(coord.getX()));
						circle.translateYProperty().bind(squareSize.add(0.25).multiply(coord.getY()));
						circle.scaleXProperty().bind(squareSize.divide(30));
						circle.scaleYProperty().bind(squareSize.divide(30));
						circle.centerXProperty().bind(squareSize.divide(2));
						circle.centerYProperty().bind(squareSize.divide(2));
						shots[boardNumber].getChildren().add(circle);	
					}

					turnIsOver.set(true);
					
					// Hide hovering squares
					hoveringSquare1.setVisible(false);
					hoveringSquare2.setVisible(false);
				} else {
					// This coordinate is NOT shootable
					System.out.println(coord + " is NOT shootable.");
				}
			});
			
			square.setOnMouseMoved((event) -> {
				// Show a hovering square if the mouse is placed over a grid
				
				if (turnIsOver.get()) { return; }; // When turn is over, do not display hovering squares
				int targetHash = event.getTarget().hashCode();
				if (game.playerInTurnValueProperty().get() == 1 && squareCoords1.containsKey(targetHash)) {
					// If on grid 1

					Rectangle target = (Rectangle) event.getTarget();
					XY coord = squareCoords1.get(targetHash);		
					if (game.isShootable(coord)) {
						hoveringSquare1.setFill(transparentGreen);
					} else {
						hoveringSquare1.setFill(transparentRed);						
					}

					Bounds squareBounds = grids[0].getCellBounds(coord.getX(), coord.getY());
					hoveringSquare1.setTranslateX(squareBounds.getMinX());
					hoveringSquare1.setTranslateY(squareBounds.getMinY());
					hoveringSquare1.setVisible(true);
				} else if (game.playerInTurnValueProperty().get() == 0 && squareCoords2.containsKey(targetHash)) {
					// If on grid 2
					XY coord = squareCoords2.get(targetHash);		
					Rectangle target = (Rectangle) event.getTarget();
					if (game.isShootable(coord)) {
						hoveringSquare2.setFill(transparentGreen);
					} else {
						hoveringSquare2.setFill(transparentRed);						
					}
					Bounds squareBounds = grids[1].getCellBounds(coord.getX(), coord.getY());
					hoveringSquare2.setTranslateX(squareBounds.getMinX());
					hoveringSquare2.setTranslateY(squareBounds.getMinY());
					hoveringSquare2.setVisible(true);				
				}
			});
			grid.add(square, (i) % gameboardSize, Math.floorDiv(i, gameboardSize),1,1);
		}
		grid.setGridLinesVisible(true);
	}
	
	@FXML
	public void handleSwitchTurn1BtnClick(ActionEvent event) {
		turnIsOver.set(false);
		gridStack1.setVisible(false);
		gridStack2.setVisible(false);
		turnInfoText.setText("Vuoron vaihto.");
		changePlayerAlert.showAndWait();
		turnInfoText.setText(String.format("Pelaajan %s vuoro ampua.",game.playerInTurnNameProperty().get()));
		//shots[game.playerInTurnNameProperty().get()].setVisible(true);
		//shots[game.playerInTurnNameProperty().get()].setVisible(false);
		gridStack1.setVisible(true);
		gridStack2.setVisible(true);
	}
	
	@FXML
	public void handleSwitchTurn2BtnClick(ActionEvent event) {
		turnIsOver.set(false);
		gridStack1.setVisible(false);
		gridStack2.setVisible(false);
		turnInfoText.setText("Vuoron vaihto.");
		changePlayerAlert.showAndWait();
		turnInfoText.setText(String.format("Pelaajan %s vuoro ampua.",game.playerInTurnNameProperty().get()));
		gridStack1.setVisible(true);
		gridStack2.setVisible(true);
	}

}
