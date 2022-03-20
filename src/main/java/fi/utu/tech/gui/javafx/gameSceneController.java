package fi.utu.tech.gui.javafx;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class gameSceneController {
	private BattleshipGame game = MainApp.getGame();
	private Group elements = new Group();
	private Group background = new Group();
	private Group[] shots = {new Group(), new Group()};
	private int gameboardSize = 10;
	private Alert changePlayerAlert = new Alert(AlertType.INFORMATION);
	private ImageView seaImageView;
	private ImageView explosionImageView;
	private ImageView splashImageView;
	private Sprite explosionAnimation;
	private Sprite splashAnimation;
	
	public gameSceneController() {
		seaImageView = new ImageView(ResourceLoader.image("sea_texture.jpg"));
		seaImageView.setViewport(new Rectangle2D(0, 0, 800, 800));
		background.getChildren().add(seaImageView);
		explosionImageView = new ImageView();
		explosionAnimation = new Sprite(explosionImageView,
										new Image(ResourceLoader.image("explosion_cropped_2.png"), 640,480,false,false),
										8, // Columns
										6, // Rows
										80, // Frame width
										80, // Frame height
										60, // FPS
										1); // Repeats
		splashImageView = new ImageView();
		splashAnimation = new Sprite(splashImageView,
										new Image(ResourceLoader.image("water_splash.png"), 400,160,false,false),
										5, // Columns
										2, // Rows
										80, // Frame width
										80, // Frame height
										40, // FPS
										1); // Repeats
	}
	
	@FXML
    private Button endGameButton;
	
	@FXML
	private Pane gameboardPane;
	
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
			background.getChildren().add(verticaLine);
			background.getChildren().add(horizontalLine);
		}
		
		shots[game.playerInTurnValueProperty().get()].setVisible(false);
		shots[game.getOpponent().ordinal()].setVisible(true);
		
		elements.getChildren().add(background);
		elements.getChildren().add(shots[0]);
		elements.getChildren().add(shots[1]);
		elements.getChildren().add(explosionImageView);
		elements.getChildren().add(splashImageView);
		gameboardPane.getChildren().add(elements);
		//changePlayerAlert.showAndWait();
	}
	
	@FXML
	public void handleGameboardClick(MouseEvent event) {
		
		// Convert the corresponding mouse coordinates to board target coordinates
		XY targetCoord = new XY((int) Math.floor(event.getX() / gameboardPane.getWidth() * gameboardSize),
				(int) Math.floor(event.getY() / gameboardPane.getHeight() * gameboardSize));
		
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
			splashImageView.setTranslateX(gameboardPane.getWidth() / gameboardSize * targetCoord.getX());
			splashImageView.setTranslateY(gameboardPane.getHeight() / gameboardSize * targetCoord.getY());
			splashAnimation.start();	
		} else if (result == 1) {
			explosionImageView.setTranslateX(gameboardPane.getWidth() / gameboardSize * targetCoord.getX());
			explosionImageView.setTranslateY(gameboardPane.getHeight() / gameboardSize * targetCoord.getY());
			explosionAnimation.start();
			// Hit on target
			Circle circle = new Circle(0,0,10);
			circle.setFill(Color.RED);
			circle.centerXProperty().bind(gameboardPane.widthProperty().divide(gameboardSize).multiply(targetCoord.getX()));
			circle.centerYProperty().bind(gameboardPane.heightProperty().divide(gameboardSize).multiply(targetCoord.getY()));
			circle.translateXProperty().bind(gameboardPane.widthProperty().divide(gameboardSize*2));
			circle.translateYProperty().bind(gameboardPane.heightProperty().divide(gameboardSize*2));
			shots[game.playerInTurnValueProperty().get()].getChildren().add(circle);	
		}

		Thread waitThread = new Thread(new Runnable() {
		      @Override
		      public void run() {

		          try {
		            Thread.sleep(2000);
		          } catch (InterruptedException e) {
		            e.printStackTrace();
		          }

		          Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		        		shots[game.playerInTurnValueProperty().get()].setVisible(false);
		        		shots[game.getOpponent().ordinal()].setVisible(true);
		            	changePlayerAlert.showAndWait();
		            }
		          });
		        };
		      });
		waitThread.start();
	};

	@FXML
    public void handleEndGameButtonClick(ActionEvent event) {		
	};

}
