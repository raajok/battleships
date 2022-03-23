package fi.utu.tech.gui.javafx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.stage.Stage;

public class gameSceneController {
	private BattleshipGame game = MainApp.getGame();
	private Group[] elements = {new Group(), new Group()};
	private Group[] hoveringSquare = {new Group(), new Group()};
	private Group[] background = {new Group(), new Group()};
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
	private Map<Integer, XY> squaresMapping = new HashMap<Integer, XY>();
	
	public gameSceneController() {		
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
		// Background image
		Image img = new Image(ResourceLoader.image("sea_texture.jpg"));
		BackgroundImage bgndImg = new BackgroundImage(img,
				BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
		Background bg1 = new Background(bgndImg);
		grids[0].setBackground(bg1);
		grids[1].setBackground(new Background(bgndImg));
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
		// Binding for "player in turn -info"
		turnInfoText.textProperty().bind(Bindings.createStringBinding(() -> 
											String.format("Pelaajan '%s' vuoro ampua.",
													game.playerInTurnNameProperty().get()), 
											game.playerInTurnNameProperty()));

		squareSize.bind(Bindings.min(scene.widthProperty().divide(2).subtract(gameboardSize*5),
				scene.heightProperty()
				.subtract(headerBox.heightProperty())
				.subtract(gameboardSize*5)
				).divide(gameboardSize));
		
		// Create a test game
		game.newGameTest("name1", "name2", gameboardSize);
		
		// Pop Up Dialog Window for player change
		changePlayerAlert.setTitle("Toisen pelaajan vuoro.");
		changePlayerAlert.setHeaderText("Pelaajan vaihto.");
		changePlayerAlert.contentTextProperty().bind(turnInfoText.textProperty());

		
		// Render the grids
		renderGrid(grids[0], gridStack1);
		renderGrid(grids[1], gridStack2);
		
		shots[game.playerInTurnValueProperty().get()].setVisible(false);
		shots[game.getOpponent().ordinal()].setVisible(true);
		
		// Panel1 elements
		elements[0].getChildren().addAll(grids[0],
										 shots[1],
										 explosionImageView1,
										 splashImageView1);
		gridStack1.getChildren().add(elements[0]);
		
		// Panel2 elements
		elements[1].getChildren().addAll(grids[1],
										 shots[0],
										 explosionImageView2,
										 splashImageView2);
		gridStack2.getChildren().add(elements[1]);
		
	}
	
	
	private void renderGrid(GridPane grid, StackPane pane) {
        grid.setAlignment(Pos.CENTER);
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
			squaresMapping.put(square.hashCode(), new XY((i) % gameboardSize, Math.floorDiv(i, gameboardSize)));
			GridPane.setHgrow(square, Priority.ALWAYS);
			GridPane.setVgrow(square, Priority.ALWAYS);
			
			square.setOnMouseClicked((event) -> {
				System.out.println(squaresMapping.get(event.getSource().hashCode()));
			});
			grid.add(square, (i) % gameboardSize, Math.floorDiv(i, gameboardSize),1,1);
		}
		grid.setGridLinesVisible(true);
	}
	
	/**
	 * The handleGameboardClick function is called when the user clicks on a square of the gameboard.
	 * It converts the mouse coordinates to board target coordinates, shoots to that target, and updates 
	 * the GUI accordingly.
	 * 
	 * A thread object is created that is used to wait for a certain amount of time.
	 * Finally, a dialog window is displayed to inform players to switch turns.
	 * 
	 * @param event Used to Get the coordinates of the mouse click.
	 * 
	 * @doc-author j-code
	 */
	
	@FXML
	public void handleSwitchTurn1BtnClick(ActionEvent event) {
		
	}
	
	@FXML
	public void handleSwitchTurn2BtnClick(ActionEvent event) {
		
	}

}
