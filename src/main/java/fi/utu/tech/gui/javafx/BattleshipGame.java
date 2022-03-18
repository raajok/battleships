package fi.utu.tech.gui.javafx;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

class Gameboard {
	// THIS STUB IS TO BE REMOVED AFTER THE PROPER GAMEBOARD CLASS HAS BEEN IMPLEMENTED
	private int nHitsRemaining;
	private SimpleBooleanProperty ready = new SimpleBooleanProperty(false);
	private int[][] board;

	public Gameboard(String playerName, int boardSize, SimpleIntegerProperty[] shipCounts) {
		// TODO Auto-generated constructor stub
		this.board = new int[boardSize][boardSize];
		
		for (int x = 0; x < boardSize; x++) {
			for (int y = 0; y < boardSize; y++) {
				board[x][y] = 0;
			}
		}
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
		return board[coord.getX()][coord.getY()] >= 0? true: false;
	}
	
	public Boolean isHitSuccessful(XY coord) {
		// STUB
		return board[coord.getX()][coord.getY()] == 1? true: false;
	}
	
	public void setHit(XY coord) {
		// STUB
		if (isHitSuccessful(coord)) {
			// Target is hit
			board[coord.getX()][coord.getY()] = -2;
		} else {
			// A miss
			board[coord.getX()][coord.getY()] = -1;
		}
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
	
	private Deque<XY> getShipCoords(Ship ship) {
		Deque<XY> coords = new ArrayDeque<XY>();
		int x = ship.getLocation().getX();
		int y = ship.getLocation().getY();
		
		switch (ship.getOrientation()) {
			case  RIGHT: for (int i = 0; i < ship.getSize(); i++) {	coords.push(new XY(x+i,y)); } break;
			case  DOWN: for (int i = 0; i < ship.getSize(); i++) {	coords.push(new XY(x,y+i)); } break;
			case  LEFT: for (int i = 0; i < ship.getSize(); i++) {	coords.push(new XY(x-i,y)); } break;
			case  UP: for (int i = 0; i < ship.getSize(); i++) {	coords.push(new XY(x,y-i)); } break;
		};
		return coords;
	}
	
	public boolean addShip(Ship ship) {
		// STUB
		for (XY coord: getShipCoords(ship)) {
			this.board[coord.getX()][coord.getY()] = 1;			
		};
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

	public int[][] getBoard() {
		// TODO Auto-generated method stub
		return board;
	}
	
	public String toString() {

		// ONLY FOR TESTING. REMOVE THIS
		StringBuilder sb = new StringBuilder();		
		sb.append("  0123456789\n");
		for (int y = 0; y < this.board.length; y++) {
			sb.append(y + " ");
			for (int x = 0; x < this.board.length; x++) {
				sb.append(this.board[x][y]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
}

public class BattleshipGame {	
	private Gameboard[] boards = new Gameboard[2];
	private SimpleStringProperty[] playerNamesProperty = {new SimpleStringProperty("Pelaaja 1"),
														  new SimpleStringProperty("Pelaaja 2")};
	private Player playerInTurn;
	private SimpleBooleanProperty gameReady = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty settingsReady = new SimpleBooleanProperty(false);
	private SimpleIntegerProperty[] shipCountProperties = {new SimpleIntegerProperty(0),
	                                              		   new SimpleIntegerProperty(0),
	                                              		   new SimpleIntegerProperty(0),
	                                              		   new SimpleIntegerProperty(0),
	                                              		   new SimpleIntegerProperty(0)};
	private SimpleIntegerProperty boardSizeProperty = new SimpleIntegerProperty(10);
	private SimpleIntegerProperty shipSums = new SimpleIntegerProperty(0);
	
	public BattleshipGame() {
		shipSums.bind(shipCountProperties[ShipType.CARRIER.ordinal()].multiply(5)
				.add(shipCountProperties[ShipType.BATTLESHIP.ordinal()].multiply(4)
				.add(shipCountProperties[ShipType.CRUISER.ordinal()].multiply(3)
				.add(shipCountProperties[ShipType.SUBMARINE.ordinal()].multiply(3)
				.add(shipCountProperties[ShipType.DESTROYER.ordinal()].multiply(2)
						)))));
		settingsReady.bind(
			 Bindings.when(
					boardSizeProperty.multiply(boardSizeProperty).greaterThanOrEqualTo(shipSums.multiply(2)))
			 		.then(true)
			 		.otherwise(false));
	}
	
	public void newGame() {
		/**
		 * The newGame function is used to start a new game.
		 * This function creates a new Gameboard object for each player with a given name and ship counts.
		 * 
		 * The boards[].readyProperty indicates a boolean state when a board is ready to start a new game.
		 * Both boards ready properties are bound to gameReady property.
		 * Likewise, the gameReady property indicates a boolean state when the game is ready to start,
		 * but it is true only when both boards are ready to start.
		 * Before binding happens, the gameReady property unbinds itself from the old bindings (if any).
		 * This way we can have only one listener on the game readiness state instead of two listeners for each board separately.
		 *
		 * Finally it sets our current playerInTurn variable as Player 1, who is a starting player.
		 * 
		 * @return Nothing.
		 * 
		 * @doc-author j-code
		 **/
		
		 // Create and initialize the boards
		 this.boards[Player.PLAYER1.ordinal()] = new Gameboard(playerNamesProperty[Player.PLAYER1.ordinal()].toString(),
				 											   boardSizeProperty.get(),
				 											   shipCountProperties);
		 this.boards[Player.PLAYER2.ordinal()] = new Gameboard(playerNamesProperty[Player.PLAYER2.ordinal()].toString(),
															   boardSizeProperty.get(),
															   shipCountProperties);
		 
		 // Create bindings for when boards are ready
		 if (this.gameReady.isBound()) this.gameReady.unbind(); // Remove old bindings
		 this.gameReady.bind(
				 Bindings.when(
						boards[Player.PLAYER1.ordinal()].readyProperty()
						.and(
				 		boards[Player.PLAYER2.ordinal()].readyProperty()))
				 		.then(true)
				 		.otherwise(false));
		 
		 // Player 1 will start the game
		 this.playerInTurn = Player.PLAYER1;
	}
	
	public void newGameTest(String name1, String name2, int bSize) {
		// ONLY FOR TESTING. REMOVE THIS
		
		// Create and initialize the boards
		this.boards[Player.PLAYER1.ordinal()] = new Gameboard(name1,bSize,shipCountProperties);
		this.boards[Player.PLAYER2.ordinal()] = new Gameboard(name2,bSize,shipCountProperties);
		
		this.boards[Player.PLAYER2.ordinal()].addShip(ShipType.BATTLESHIP.instantiate(new XY(2,2), Orientation.RIGHT));
		this.boards[Player.PLAYER2.ordinal()].addShip(ShipType.CRUISER.instantiate(new XY(8,8), Orientation.LEFT));
				 
		// Player 1 will start the game
		this.playerInTurn = Player.PLAYER1;
	}
	
	public int shootTest(XY coord) {
		// ONLY FOR TESTING. REMOVE THIS
		Gameboard opponentBoard = boards[getOpponent().ordinal()];
		int valueAtLocation = opponentBoard.getBoard()[coord.getX()][coord.getY()];
		if (opponentBoard.isShootable(coord)) {
			opponentBoard.setHit(coord);
			// Is game over?
			if (opponentBoard.getnHitsRemaining() == 0) {
				// TODO
				// player "playerInTurn" has won.
				// Switch to "Start Menu"-scene and announce the winner.
			}
			// If game continues, switch turns.
			//switchTurn();
		}
		return valueAtLocation;
	}
	
	public int shoot(XY coord) {
		Gameboard opponentBoard = boards[getOpponent().ordinal()];
		int valueAtLocation = opponentBoard.getBoard()[coord.getX()][coord.getY()];
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
		}
		return valueAtLocation;
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
	
	public SimpleBooleanProperty gameReadyProperty() {
		return this.gameReady;
	}
	
	public SimpleBooleanProperty settingsReadyProperty() {
		return this.settingsReady;
	}
	
	public SimpleIntegerProperty[] shipCountProperties() {
		return shipCountProperties;
	}

	public SimpleIntegerProperty boardSizeProperty() {
		return boardSizeProperty;
	}
	
	public SimpleStringProperty[] getPlayerNamesProperty() {
		return playerNamesProperty;
	}
	
	public Gameboard getBoard() {
		return boards[playerInTurn.ordinal()];
	}
	
	public Gameboard getOpponentBoard() {
		return boards[getOpponent().ordinal()];
	}
}
