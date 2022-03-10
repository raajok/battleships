package fi.utu.tech.gui.javafx;

class Gameboard {
	// THIS STUMP IS TO BE REMOVED AFTER THE PROPER GAMEBOARD CLASS IS IMPLEMENTED

	public Gameboard(String playerName, int boardSize, int[] shipCounts) {
		// TODO Auto-generated constructor stub
	}
	
	
}

public class BattleshipGame {
	private Gameboard board1;
	private Gameboard board2;
	private int playerTurn;
	
	public void newGame(String playerName1, String playerName2, int boardSize, int[] shipCounts) {
		// Initialize a new game
		 this.board1 = new Gameboard(playerName1, boardSize, shipCounts);
		 this.board2 = new Gameboard(playerName2, boardSize, shipCounts);
		 this.playerTurn = 1; // Player 1 will start the game
	}
	
	public void shoot(Gameboard opponentBoard, int x, int y) {
		// TODO
	}
	
	public Boolean isGameOver() {
		// TODO
		return null;
	}

}
