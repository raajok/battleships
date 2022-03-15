package fi.utu.tech.gui.javafx;

import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;

class Gameboard {
	// THIS STUB IS TO BE REMOVED AFTER THE PROPER GAMEBOARD CLASS HAS BEEN IMPLEMENTED
	private int nHitsRemaining;
	private List<Ship> ships;
	private SimpleBooleanProperty ready = new SimpleBooleanProperty(false);

	public Gameboard(String playerName, int boardSize, int[] shipCounts) {
		// TODO Auto-generated constructor stub
	}
	
	public XY getNearestCoordinate(XY mouseXY) {
		// Deprecated
		return null;
	}
	
	public Ship createShip(ShipType shipType) {
		// STUB
		return null;
	}
	
	public void setShip(Ship ship, XY coord) {
		// STUB
	}
	
	public Boolean isShootable(XY coord) {
		// STUB
		return null;
	}
	
	public Boolean isHitSuccessful(XY coord) {
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
	
	public boolean addShip(Ship ship) {
		// STUB
		return true;
	}
	
	public boolean moveShip(Ship ship, XY toLocation) {
		// STUB
		return true;
	}
	
	public SimpleBooleanProperty readyProperty() {
		// STUB
		return this.ready;
	}
	
}

public class BattleshipGame {	
	private Gameboard[] boards = new Gameboard[2];
	private Player playerInTurn;
	private SimpleBooleanProperty ready = new SimpleBooleanProperty(false);
	private int[] shipCounts = new int[ShipType.values().length];
	
	public void newGame(String playerName1, String playerName2, int boardSize, int[] shipCounts) {
		// Initialize a new game
		 this.boards[Player.PLAYER1.ordinal()] = new Gameboard(playerName1, boardSize, shipCounts);
		 this.boards[Player.PLAYER2.ordinal()] = new Gameboard(playerName2, boardSize, shipCounts);
		 this.playerInTurn = Player.PLAYER1; // Player 1 will start the game
		 this.shipCounts = shipCounts;
		 
		 // Create bindings for when boards are ready
		 if (this.ready.isBound()) this.ready.unbind(); // Remove old bindings
		 this.ready.bind(
				 Bindings.when(
						boards[Player.PLAYER1.ordinal()].readyProperty()
						.and(
				 		boards[Player.PLAYER2.ordinal()].readyProperty()))
				 		.then(true)
				 		.otherwise(false));
	}
	
	public void shoot(XY coord) {
		Gameboard opponentBoard = boards[getOpponent().ordinal()];
		if (opponentBoard.isShootable(coord)) {
			opponentBoard.setHit(coord);
			// Is game over?
			if (opponentBoard.getnHitsRemaining() == 0) {
				// TODO
				// player "playerInTurn" has won.
				// Switch to "Start Menu"-scene and announce the winner.
			}
			// If game continues, switch turns.
			switchTurn();
		} else {
			System.err.println("Exception: This location is not shootable.");
		}
	}
	
	public Boolean isGameOver() {
		// deprecated
		return null;
	}
	
	private void switchTurn() {
		this.playerInTurn = getOpponent(this.playerInTurn);
	}
	
	private Player getOpponent() {
		return getOpponent(this.playerInTurn); 
	}
	
	private Player getOpponent(Player player) {
		return player.next(); 
	}
	
	public boolean addShip(Ship ship) {
		return addShip(ship, this.playerInTurn);
	}
	
	public boolean addShip(Ship ship, Player player) {
		return boards[player.ordinal()].addShip(ship);
	}
	
	public Ship createShip(ShipType shipType, XY location, Orientation orientation) {
		return shipType.instantiate(location, orientation);
	}
	
	public SimpleBooleanProperty readyProperty() {
		return this.ready;
	}

}
