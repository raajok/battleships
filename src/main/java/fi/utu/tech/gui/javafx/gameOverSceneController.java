package fi.utu.tech.gui.javafx;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class gameOverSceneController {

	private BattleshipGame game = MainApp.getGame();
	
	@FXML
	private HBox rootBox;
	
    @FXML
    private Button endGameButton;

    @FXML
    private Button playAgainButton;

    @FXML
    private Text playerWonText;

    @FXML
    void endGame(ActionEvent event) {
    	Platform.exit();
    }
    
    public Button getPlayAgainButton() {
    	return playAgainButton;
    }
    
    @FXML
    public void initialize() {

		// Set background color
		this.rootBox.setStyle("-fx-background-color: lightsteelblue;");
		
    	playerWonText.textProperty().bind(Bindings.createStringBinding(() -> 
    		String.format("%s voitti!", game.playerInTurnNameProperty().get()), game.playerInTurnNameProperty()));
    }
}
