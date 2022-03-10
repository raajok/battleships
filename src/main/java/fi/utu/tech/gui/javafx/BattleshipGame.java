package fi.utu.tech.gui.javafx;

class XY {
	private final int x;
	private final int y;
	
	public XY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}

class Gameboard {
	// THIS STUMP IS TO BE REMOVED AFTER A PROPER GAMEBOARD CLASS HAS BEEN IMPLEMENTED

	public Gameboard(String playerName, int boardSize, int[] shipCounts) {
		// TODO Auto-generated constructor stub
	}
	
	public XY getNearestCoordinate(XY mouseXY) {
		// STUMP
		return null;
	}
	
	public Ship createShip(ShipType shipType) {
		// STUMP
		return null;
	}
	
	public void setShip(Ship ship, int x, int y) {
		// STUMP
	}
	
	public Boolean isShootable(int x, int y) {
		// STUMP
		return null;
	}
	
	public Boolean isHitSuccessful(int x, int y) {
		// STUMP
		return null;
	}
	
	public void setHit(XY coord) {
		// STUMP
	}
	
	public void reset() {
		// STUMP
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
	
	public void shoot(XY mouseXY) {
		int opponent = getOpponent(this.playerTurn);
		Gameboard opponentBoard = boards[opponent];
		XY coord = opponentBoard.getNearestCoordinate(mouseXY);
		if (opponentBoard.isShootable(coord.getX(), coord.getY())) {
			opponentBoard.setHit(coord);
			// After a successful shooting, switch turns.
			switchTurn();
		} else {
			System.err.println("Error: This location is not shootable.");
		}
	}
	
	public Boolean isGameOver() {
		// TODO
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

}
