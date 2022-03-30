package fi.utu.tech.gui.javafx;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

/*
 * Controller for the StartMenu scene.
 */
public class StartMenuController {
	
	private BattleshipGame game = MainApp.getGame();
	
	/*
	 * Called when the loader in MainApp is created. Creates all the needed bindings and 
	 * instantiates some of the scene's components.
	 */
	public void initialize() {
		
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
		this.game.getPlayerNamesProperty()[Player.PLAYER1.ordinal()].bind(player1.textProperty());
		this.game.getPlayerNamesProperty()[Player.PLAYER2.ordinal()].bind(player2.textProperty());
		
		// Binding for start button availability to settings ready property
		startButton.disableProperty().bind(this.game.settingsReadyProperty().not());
	}
	
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
    
    // Called when the "Lopeta"-button is pressed. Closes the app.
    @FXML
    public void EndGame() {
    	Platform.exit();
    }
    
    public void startGame() {
    	// TODO: check if ratio of board and ships is ok before allowing start button to be pressed
    	
    	game.newGame();
    }
    
    public Button getStartButton() {
    	return startButton;
    }
}
