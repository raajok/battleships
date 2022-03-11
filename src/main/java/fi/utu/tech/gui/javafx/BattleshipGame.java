package fi.utu.tech.gui.javafx;

class Gameboard {
	// THIS STUB IS TO BE REMOVED AFTER THE PROPER GAMEBOARD CLASS HAS BEEN IMPLEMENTED
	private int nHitsRemaining;

	public Gameboard(String playerName, int boardSize, int[] shipCounts) {
		// TODO Auto-generated constructor stub
	}
	
	public XY getNearestCoordinate(XY mouseXY) {
		// STUB
		return null;
	}
	
	public Ship createShip(ShipType shipType) {
		// STUB
		return null;
	}
	
	public void setShip(Ship ship, int x, int y) {
		// STUB
	}
	
	public Boolean isShootable(int x, int y) {
		// STUB
		return null;
	}
	
	public Boolean isHitSuccessful(int x, int y) {
		// STUB
		return null;
	}
	
	public void setHit(XY coord) {
		// STUB
	}
	
	public void reset() {
		// STUB
	}
	
	public int getnHitsRemaining() {
		// STUB
		return nHitsRemaining;
	}
	
	public void setnHitsRemaining(int nHitsRemaining) {
		// STUB
		this.nHitsRemaining = nHitsRemaining;
	}
	
}

public class BattleshipGame {
	private final int player1 = 1;
	private final int player2 = 2;
	private Gameboard[] boards = new Gameboard[2];
	private int playerTurn;
	
	public void newGame(String playerName1, String playerName2, int boardSize, int[] shipCounts) {
		// Initialize a new game
		 this.boards[0] = new Gameboard(playerName1, boardSize, shipCounts);
		 this.boards[1] = new Gameboard(playerName2, boardSize, shipCounts);
		 this.playerTurn = player1; // Player 1 will start the game
	}
	
	public void shoot(XY coord) {
		Gameboard opponentBoard = boards[getOpponent()];
		if (opponentBoard.isShootable(coord.getX(), coord.getY())) {
			opponentBoard.setHit(coord);
			// Is game over?
			if (opponentBoard.getnHitsRemaining() == 0) {
				// TODO Switch to "Start Menu"-scene and announce the winner.
			}
			// If game continues, switch turns.
			switchTurn();
		} else {
			System.err.println("Error: This location is not shootable.");
		}
	}
	
	public Boolean isGameOver() {
		// deprecated
		return null;
	}
	
	private void switchTurn() {
		this.playerTurn = getOpponent(this.playerTurn);
	}
	
	private int getOpponent() {
		return getOpponent(this.playerTurn); 
	}
	
	private int getOpponent(int player) {
		return player % 2 + 1; 
	}
	
	public void setShip(Ship ship, XY coord) {
		Gameboard board = boards[this.playerTurn];
	}

}
