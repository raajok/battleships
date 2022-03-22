package fi.utu.tech.gui.javafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class gameOverSceneController {

	private BattleshipGame game = MainApp.getGame();
	
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
    
    public void setWinner() {
    	playerWonText.setText(game.getPlayerInTurn().toString() + " voitti!");
    }
}
