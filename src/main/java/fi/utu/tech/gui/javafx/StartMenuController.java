package fi.utu.tech.gui.javafx;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/*
 * Controller for the StartMenu scene.
 */
public class StartMenuController {
	private Group soundBoxContainer = new Group();
	
	private BattleshipGame game = MainApp.getGame();
	
	/*
	 * Called when the loader in MainApp is created. Creates all the needed bindings and 
	 * instantiates some of the scene's components.
	 */
	public void initialize() {
		
		Image bgImg = new Image(ResourceLoader.image("startMenuBG.jpg"));
		

	    BackgroundSize bSize = new BackgroundSize(1.0, 1.0, true, true, false, true);

	    Background background = new Background(new BackgroundImage(bgImg,
	            BackgroundRepeat.NO_REPEAT,
	            BackgroundRepeat.NO_REPEAT,
	            BackgroundPosition.CENTER,
	            bSize));

	    upperBox.setBackground(background);
		
		carrierSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1));
		battleshipSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1));
		cruiserSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1));
		submarineSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1));
		destroyerSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1));
		
		// Binding for the slider label to the slider value property
		sliderLabel.textProperty().bind(Bindings.createStringBinding(() -> 
			String.format("%1$s x %1$s", Math.round(slider.getValue())), slider.valueProperty()));
		
		// Bindings for ship counts to spinner value properties
		this.game.shipCountProperties()[ShipType.CARRIER.ordinal()].bind(carrierSpinner.valueProperty());
		this.game.shipCountProperties()[ShipType.BATTLESHIP.ordinal()].bind(battleshipSpinner.valueProperty());
		this.game.shipCountProperties()[ShipType.CRUISER.ordinal()].bind(cruiserSpinner.valueProperty());
		this.game.shipCountProperties()[ShipType.SUBMARINE.ordinal()].bind(submarineSpinner.valueProperty());
		this.game.shipCountProperties()[ShipType.DESTROYER.ordinal()].bind(destroyerSpinner.valueProperty());
		
		// Binding for the board size to the slider value property
		this.game.boardSizeProperty().bind(
			Bindings.createIntegerBinding(() -> (int) Math.round(slider.getValue()), slider.valueProperty()));
		
		// Binding for the player names to the player TextFields
		this.game.getPlayerNamesProperty()[Player.PLAYER1.ordinal()].bindBidirectional(player1.textProperty());
		this.game.getPlayerNamesProperty()[Player.PLAYER2.ordinal()].bindBidirectional(player2.textProperty());
    	
		// Binding for start button availability to settings ready property
		startButton.disableProperty().bind(this.game.settingsReadyProperty().not());
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		        startButton.requestFocus();
		    }
		});
	}
	
	@FXML
	private VBox upperBox;
	
	@FXML
    private Button startButton;
	
	@FXML
    private Button endButton;
	
    @FXML
    private Spinner<Integer> battleshipSpinner;

    @FXML
    private Spinner<Integer> carrierSpinner;

    @FXML
    private Spinner<Integer> cruiserSpinner;

    @FXML
    private Spinner<Integer> destroyerSpinner;

    @FXML
    private Spinner<Integer> submarineSpinner;
    
    @FXML
    private TextField player1;

    @FXML
    private TextField player2;

    @FXML
    private Slider slider;

    @FXML
    private Label sliderLabel;
    
    @FXML
    private StackPane mainStackPane;
    
    @FXML
    private ImageView soundBoxIcon;
    
    // Called when the "Lopeta"-button is pressed. Closes the app.
    @FXML
    public void EndGame() {
    	Platform.exit();
        System.exit(0);
    }
    
    @FXML
    public void onSoundBoxMouseEntered(MouseEvent event) {
    	
    }
    
    public void startGame() {    	
    	game.newGame();
    }
    
    public Button getStartButton() {
    	return startButton;
    }
    
    public void setSoundBox(Group soundBoxContainer) {
    	this.soundBoxContainer = soundBoxContainer;
    	mainStackPane.widthProperty().addListener((obj, oldVal,newVal) -> {
    		this.soundBoxContainer.setTranslateX(newVal.doubleValue() / 2 - this.soundBoxContainer.getBoundsInLocal().getMaxX() / 2 - 20);
    	});
    	this.soundBoxContainer.setTranslateY(20);
    	mainStackPane.getChildren().add(this.soundBoxContainer);
	}
}
