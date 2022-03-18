package fi.utu.tech.gui.javafx;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;


public class StartMenuController {

	private BattleshipGame game;
	
	public void initialize() {
		game = new BattleshipGame();
		
		carrierSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1));
		battleshipSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 2));
		cruiserSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 3));
		submarineSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 4));
		destroyerSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 5));
		
		// Binding for the slider label to the slider value property
		sliderLabel.textProperty().bind(Bindings.createStringBinding(() -> 
			String.format("%1$s x %1$s", Math.round(slider.getValue())), slider.valueProperty()));
		
		// Bindings for ship counts to spinner value properties
		game.shipCountProperties()[ShipType.CARRIER.ordinal()].bind(carrierSpinner.valueProperty());
		game.shipCountProperties()[ShipType.BATTLESHIP.ordinal()].bind(battleshipSpinner.valueProperty());
		game.shipCountProperties()[ShipType.CRUISER.ordinal()].bind(cruiserSpinner.valueProperty());
		game.shipCountProperties()[ShipType.SUBMARINE.ordinal()].bind(submarineSpinner.valueProperty());
		game.shipCountProperties()[ShipType.DESTROYER.ordinal()].bind(destroyerSpinner.valueProperty());
		
		// Binding for the board size to the slider value property
		game.boardSizeProperty().bind(
				Bindings.createIntegerBinding(() -> (int) Math.round(slider.getValue()), slider.valueProperty()));
		
		// Binding for start button availability to settings ready property
		startButton.disableProperty().bind(game.settingsReadyProperty().not());
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
    
    @FXML
    public void startGame() {
    	// TODO: check if ratio of board and ships is ok before allowing start button to be pressed
    	
    	game.newGame(player1.getText(), player2.getText());
    }
    
    public int[] getBattleships() {
    	return new int[] {
			carrierSpinner.getValue(),
			battleshipSpinner.getValue(),
			cruiserSpinner.getValue(),
			submarineSpinner.getValue(),
			destroyerSpinner.getValue()
    	};
    }
}
